plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.hilt.android.plugin)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.spotless)
}

android {
    namespace = "com.juanpaxi.sini"
    compileSdk {
        version =
            release(
                libs.versions.compileSdk
                    .get()
                    .toInt(),
            )
    }
    defaultConfig {
        applicationId = "com.juanpaxi.sini"
        minSdk =
            libs.versions.minSdk
                .get()
                .toInt()
        targetSdk =
            libs.versions.targetSdk
                .get()
                .toInt()
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    lint {
        xmlReport = true
        sarifReport = true
        checkDependencies = true
    }
    buildFeatures {
        compose = true
    }
}

spotless {
    kotlin {
        target("src/**/*.kt")
        ktlint()
        endWithNewline()
    }
    kotlinGradle {
        target("**/*.gradle.kts")
        ktlint()
        trimTrailingWhitespace()
        endWithNewline()
    }
    format("xml") {
        target("src/**/*.xml")
        endWithNewline()
    }
}

dependencies {
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.bundles.compose.ui)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
}
