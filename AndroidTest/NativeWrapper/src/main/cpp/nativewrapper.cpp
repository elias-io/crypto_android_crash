#include <jni.h>
#include <dlfcn.h>
#include <android/log.h>

namespace {
    using VoidFn = void (*)();

    constexpr const char* kTag = "NativeWrapper";
    constexpr const char* kDotNetSo = "libDotNetNativeCrypto.so";

    inline void* dotnet_handle() {
        // Resolve against a known handle (works even if the lib was loaded RTLD_LOCAL).
        static void* h = nullptr;
        if (!h) h = dlopen(kDotNetSo, RTLD_NOW);
        return h;
    }

    inline VoidFn find_export(const char* symbol) {
        void* h = dotnet_handle();
        if (!h) return nullptr;
        return reinterpret_cast<VoidFn>(dlsym(h, symbol));
    }
} // namespace

extern "C" JNIEXPORT void JNICALL
Java_com_example_nativewrapper_NativeLib_helloWorld(
        JNIEnv* /* env */,
        jobject /* this */) {
    __android_log_print(ANDROID_LOG_INFO, kTag, "helloWorld()");
    auto fn = find_export("b_hello_world");
    if (!fn) {
        __android_log_print(ANDROID_LOG_ERROR, kTag, "Missing export: b_hello_world");
        return;
    }
    fn();
}

extern "C" JNIEXPORT void JNICALL
Java_com_example_nativewrapper_NativeLib_helloWorldRandom(
        JNIEnv* /* env */,
        jobject /* this */) {
    __android_log_print(ANDROID_LOG_INFO, kTag, "helloWorldRandom()");
    auto fn = find_export("b_hello_world_random");
    if (!fn) {
        __android_log_print(ANDROID_LOG_ERROR, kTag, "Missing export: b_hello_world_random");
        return;
    }
    fn();
}