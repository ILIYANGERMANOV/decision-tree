plugins {
    id("ivyapps.android.application")
    id("ivyapps.android.application.compose")
    id("ivyapps.android.hilt")
    kotlin("kapt")
}

android {
    namespace = "com.ivyapps.decision"

    compileSdk = 33

    buildTypes {
        debug {

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

    testImplementation(libs.junit5)
    testImplementation(libs.kotest.junit5)
    testImplementation(libs.kotest.apiJvm)
    testImplementation(libs.kotest.engineJvm)
    testImplementation(libs.kotest.assertions)
    testImplementation(libs.kotest.dataset)
    testImplementation(libs.kotest.property)
    testImplementation(libs.kotest.kotlinReflect)
}