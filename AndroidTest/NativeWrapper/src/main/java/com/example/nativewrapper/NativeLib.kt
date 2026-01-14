package com.example.nativewrapper

public class NativeLib {
    /**
     * Calls DotNetNativeCrypto's unmanaged export: b_hello_world
     */
    external fun helloWorld()

    /**
     * Calls DotNetNativeCrypto's unmanaged export: b_hello_world_random
     */
    external fun helloWorldRandom()

    companion object {
        // Used to load native libraries on application startup.
        init {
            // Provided by AndroidTest/NativeWrapper/libs/DotNetNativeCrypto.aar
            System.loadLibrary("DotNetNativeCrypto")
            System.loadLibrary("nativewrapper")
        }
    }
}