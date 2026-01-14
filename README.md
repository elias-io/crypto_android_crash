## Repro
1) Build the NativeAOT .so and generate the AAR:
./build_dotnet_native_crypto_androidtest.sh

2) Open `AndroidTest/` in Android Studio.

3) Run instrumentation test:
`DotNetNativeCryptoBridgeTest.calls_dotnet_hello_world_random`

File:
`AndroidTest/NativeWrapper/src/androidTest/java/com/example/nativewrapper/DotNetNativeCryptoBridgeTest.kt`

## Expected / Actual
- `calls_dotnet_hello_world` works (just Console.WriteLine).
- `calls_dotnet_hello_world_random` crashes because it calls:
`RandomNumberGenerator.GetBytes(1)` inside NativeAOT.