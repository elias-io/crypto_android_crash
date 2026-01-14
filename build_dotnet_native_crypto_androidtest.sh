#!/usr/bin/env bash
set -e

# Bare-bones: build NativeAOT .so for Android, then zip an .aar.
# Assumes you run this from the script folder (it cd's there).

cd "$(dirname "$0")"

# Cake-like: use ANDROID_NDK_HOME if set, otherwise pick latest installed NDK under the default path.
NDK_DIR="${ANDROID_NDK_HOME:-$HOME/Library/Android/sdk/ndk/$(ls -1 "$HOME/Library/Android/sdk/ndk" | sort -V | tail -n 1)}"
CLANG="$(ls -1 "$NDK_DIR/toolchains/llvm/prebuilt/"*/bin/clang | head -n 1)"

mkdir -p ".artifacts/DotNetNativeCrypto/publish/android-arm64"
mkdir -p ".artifacts/DotNetNativeCrypto/publish/android-x64"

dotnet publish "DotNetNativeCrypto/DotNetNativeCrypto/DotNetNativeCrypto.csproj" \
  -c Release -f net10.0 -r android-arm64 \
  --output ".artifacts/DotNetNativeCrypto/publish/android-arm64/" \
  -p:PublishAot=true -p:PublishAotUsingRuntimePack=true -p:SelfContained=true -p:NativeLib=Shared \
  -p:CppCompilerAndLinker="$CLANG" -p:CppLinker="$CLANG" -p:StripSymbols=false

dotnet publish "DotNetNativeCrypto/DotNetNativeCrypto/DotNetNativeCrypto.csproj" \
  -c Release -f net10.0 -r android-x64 \
  --output ".artifacts/DotNetNativeCrypto/publish/android-x64/" \
  -p:PublishAot=true -p:PublishAotUsingRuntimePack=true -p:SelfContained=true -p:NativeLib=Shared \
  -p:CppCompilerAndLinker="$CLANG" -p:CppLinker="$CLANG" -p:StripSymbols=false

rm -rf ".artifacts/DotNetNativeCrypto/aar/staging"
mkdir -p ".artifacts/DotNetNativeCrypto/aar/staging/jni/arm64-v8a"
mkdir -p ".artifacts/DotNetNativeCrypto/aar/staging/jni/x86_64"

cp ".artifacts/DotNetNativeCrypto/publish/android-arm64/"*.so \
  ".artifacts/DotNetNativeCrypto/aar/staging/jni/arm64-v8a/libDotNetNativeCrypto.so"
cp ".artifacts/DotNetNativeCrypto/publish/android-x64/"*.so \
  ".artifacts/DotNetNativeCrypto/aar/staging/jni/x86_64/libDotNetNativeCrypto.so"

cat > ".artifacts/DotNetNativeCrypto/aar/staging/AndroidManifest.xml" <<EOF
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.yourcompany.DotNetNativeCrypto">
</manifest>
EOF

mkdir -p ".artifacts/DotNetNativeCrypto/aar/staging/_empty_classes"
echo "" > ".artifacts/DotNetNativeCrypto/aar/staging/_empty_classes/.keep"
(cd ".artifacts/DotNetNativeCrypto/aar/staging/_empty_classes" && zip -q -r "../classes.jar" .)
rm -rf ".artifacts/DotNetNativeCrypto/aar/staging/_empty_classes"

mkdir -p "AndroidTest/NativeWrapper/libs"
rm -f "AndroidTest/NativeWrapper/libs/DotNetNativeCrypto.aar"
(cd ".artifacts/DotNetNativeCrypto/aar/staging" && zip -q -r "../../../../AndroidTest/NativeWrapper/libs/DotNetNativeCrypto.aar" .)

echo "Wrote AndroidTest/NativeWrapper/libs/DotNetNativeCrypto.aar"
