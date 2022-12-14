import com.aleyn.AndroidX
import com.aleyn.BuildConfig
import com.aleyn.Depend
import com.aleyn.Retrofit

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}

android {
    compileSdk = BuildConfig.compileSdkVersion

    defaultConfig {
        minSdk = BuildConfig.minSdkVersion
        targetSdk = BuildConfig.targetSdkVersion
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}



dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    //androidx
    AndroidX.values.forEach { implementation(it) }
    //network
    Retrofit.values.forEach { api(it) }
    //material-dialogs
    api(Depend.dialogs)
    api(Depend.dialogsCore)
    //coil
    api(Depend.coil)
    // utils 集合了大量常用的工具类
    api(Depend.utilCode)
    api(Depend.BRVAH)
}