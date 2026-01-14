package com.example.myapplication

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.nativewrapper.NativeLib
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DotNetNativeCryptoBridgeFromAppTest {
    @Test
    fun calls_dotnet_nativeaot_exports_via_nativewrapper() {
        val native = NativeLib()
        native.helloWorld()
        native.helloWorldRandom()
    }
}

