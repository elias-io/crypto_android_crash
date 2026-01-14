package com.example.nativewrapper

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.nativewrapper.NativeLib
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DotNetNativeCryptoBridgeTest {

    @Test
    fun calls_dotnet_hello_world() {
        val native = NativeLib()
        print("XXXXX")
        native.helloWorld()
    }

    @Test
    fun calls_dotnet_hello_world_random() {
        val native = NativeLib()
        print("XXXXX")
        native.helloWorldRandom()
    }
}

