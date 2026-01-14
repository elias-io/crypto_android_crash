plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

val dotNetNativeCryptoAar = layout.projectDirectory.file("libs/DotNetNativeCrypto.aar")

val extractDotNetNativeCryptoJni by tasks.registering(Sync::class) {
    from(zipTree(dotNetNativeCryptoAar))
    include("jni/**")
    into(layout.buildDirectory.dir("dotnetnativecrypto-aar"))
}

android {
    namespace = "com.example.nativewrapper"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        minSdk = 29

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        // DotNetNativeCrypto currently ships only these ABIs (see jniLibs/).
        ndk {
            abiFilters += listOf("arm64-v8a", "x86_64")
        }

        externalNativeBuild {
            cmake {
                cppFlags("")
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    externalNativeBuild {
        cmake {
            path("src/main/cpp/CMakeLists.txt")
            version = "3.22.1"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    // Consume JNI libs from DotNetNativeCrypto.aar (without adding it as a direct AAR dependency).
    sourceSets.getByName("main").jniLibs.srcDir(extractDotNetNativeCryptoJni.map { it.destinationDir.resolve("jni") })
}

tasks.named("preBuild").configure {
    dependsOn(extractDotNetNativeCryptoJni)
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}