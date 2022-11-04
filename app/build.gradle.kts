plugins {
    id("ivyapps.android.application")
    id("ivyapps.android.application.compose")
    id("ivyapps.android.hilt")
    kotlin("kapt")
}

android {
    namespace = "com.ivyapps.decision"

    compileSdk = 33

    signingConfigs {
        getByName("debug") {
            storeFile = file("../debug.jks")
            storePassword = "IVY7834!DEbug"
            keyAlias = "debug"
            keyPassword = "IVY7834!DEbug"
        }

        create("release") {
            storeFile = file("../sign.jks")
            storePassword = System.getenv("SIGNING_STORE_PASSWORD")
            keyAlias = System.getenv("SIGNING_KEY_ALIAS")
            keyPassword = System.getenv("SIGNING_KEY_PASSWORD")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            isDefault = false

            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        create("demo") {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            isDefault = false

            signingConfig = signingConfigs.getByName("debug")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            applicationIdSuffix = ".demo"
        }

        debug {
            isDebuggable = true
            isMinifyEnabled = false
            isDefault = true

            signingConfig = signingConfigs.getByName("debug")

            applicationIdSuffix = ".debug"
        }
    }


    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    implementation(libs.androidx.core.ktx)
    implementation(libs.coil.kt)
    implementation(libs.coil.kt.compose)
    implementation(libs.kotlinx.datetime)

    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.foundation.layout)
    implementation(libs.androidx.compose.material.iconsExtended)
    implementation(libs.androidx.compose.material3)
    debugImplementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.ui.util)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.runtime.livedata)
    implementation(libs.androidx.metrics)
    implementation(libs.androidx.tracing.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.viewModelCompose)

    testImplementation(libs.junit5)
    testImplementation(libs.kotest.junit5)
    testImplementation(libs.kotest.apiJvm)
    testImplementation(libs.kotest.engineJvm)
    testImplementation(libs.kotest.assertions)
    testImplementation(libs.kotest.dataset)
    testImplementation(libs.kotest.property)
    testImplementation(libs.kotest.kotlinReflect)
}