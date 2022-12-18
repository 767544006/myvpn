import com.android.build.gradle.api.LibraryVariant


plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    compileSdk = 33
    defaultConfig {
        minSdk = 21
        targetSdk = 33
        externalNativeBuild {
            cmake {
            }
        }
        consumerProguardFiles("consumer-rules.pro")
    }
    ndkVersion = "24.0.8215888"
    externalNativeBuild {
        cmake {
            path("src/main/cpp/CMakeLists.txt")
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    lint {
        enable += setOf("BackButton", "EasterEgg", "StopShip", "IconExpectedSize", "GradleDynamicVersion", "NewerVersionAvailable")
        checkOnly += setOf("ImpliedQuantity", "MissingQuantity")
        disable += setOf("MissingTranslation", "UnsafeNativeCodeLocation")
    }
    lint {
        baseline = file("lint-baseline.xml")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    splits {
        abi {
            isEnable = true
            reset()
            include("x86", "x86_64", "armeabi-v7a", "arm64-v8a")
            isUniversalApk = true
        }
    }

}
var swigcmd = "swig"
// Workaround for macOS(arm64) and macOS(intel) since it otherwise does not find swig and
// I cannot get the Exec task to respect the PATH environment :(
if (file("/opt/homebrew/bin/swig").exists())
    swigcmd = "/opt/homebrew/bin/swig"
else if (file("/usr/local/bin/swig").exists())
    swigcmd = "/usr/local/bin/swig"


fun registerGenTask(variantName: String, variantDirName: String): File {
    val baseDir = File(buildDir, "generated/source/ovpn3swig/${variantDirName}")
    val genDir = File(baseDir, "net/openvpn/ovpn3")
    tasks.register<Exec>("generateOpenVPN3Swig${variantName}")
    {

        doFirst {
            mkdir(genDir)
        }
        commandLine(listOf(swigcmd, "-outdir", genDir, "-outcurrentdir", "-c++", "-java", "-package", "net.openvpn.ovpn3",
            "-Isrc/main/cpp/openvpn3/client", "-Isrc/main/cpp/openvpn3/",
            "-DOPENVPN_PLATFORM_ANDROID",
            "-o", "${genDir}/ovpncli_wrap.cxx", "-oh", "${genDir}/ovpncli_wrap.h",
            "src/main/cpp/openvpn3/client/ovpncli.i"))
        inputs.files( "src/main/cpp/openvpn3/client/ovpncli.i")
        outputs.dir( genDir)

    }
    return baseDir
}

android.libraryVariants.all(object : Action<com.android.build.gradle.api.LibraryVariant> {

    override fun execute(t: LibraryVariant) {
        val sourceDir = registerGenTask(t.name, t.baseName.replace("-", "/"))
        val task = tasks.named("generateOpenVPN3Swig${t.name}").get()

        t.registerJavaGeneratingTask(task, sourceDir)
    }
})
dependencies {
//    implementation("androidx.security:security-crypto:1.0.0")
    val preferenceVersion = "1.2.0"
    val coreVersion = "1.7.0"
    val materialVersion = "1.5.0"
    val fragment_version = "1.4.1"
    implementation("androidx.annotation:annotation:1.4.0")
    implementation("androidx.core:core:$coreVersion")
    implementation("androidx.core:core-ktx:1.8.0")
    // Is there a nicer way to do this?
     implementation( "androidx.constraintlayout:constraintlayout:2.1.4")
     implementation( "org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.6.21")
     implementation( "androidx.appcompat:appcompat:1.5.0")
     implementation( "com.squareup.okhttp3:okhttp:4.9.3")
     implementation( "androidx.core:core:$coreVersion")
     implementation( "androidx.core:core-ktx:$coreVersion")
     implementation( "androidx.fragment:fragment-ktx:$fragment_version")
     implementation( "androidx.preference:preference:$preferenceVersion")
     implementation( "androidx.preference:preference-ktx:$preferenceVersion")
     implementation( "com.google.android.material:material:$materialVersion")
     implementation( "androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
     implementation( "androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")


}