import com.android.build.api.variant.BuildConfigField
import java.io.StringReader
import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.hilt.android.plugin)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.spotless)
}

android {
    namespace = "com.juanpaxi.sini.core.network"
    compileSdk {
        version =
            release(
                libs.versions.compileSdk
                    .get()
                    .toInt(),
            )
    }
    defaultConfig {
        minSdk =
            libs.versions.minSdk
                .get()
                .toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    flavorDimensions += "contentType"
    productFlavors {
        create("demo") {
            dimension = "contentType"
        }
        create("prod") {
            dimension = "contentType"
        }
    }
    lint {
        xmlReport = true
        sarifReport = true
        checkDependencies = true
    }
    buildFeatures {
        buildConfig = true
    }
}

spotless {
    kotlin {
        target("src/**/*.kt")
        ktlint()
        trimTrailingWhitespace()
        endWithNewline()
    }
    kotlinGradle {
        target("**/*.gradle.kts")
        ktlint()
        trimTrailingWhitespace()
        endWithNewline()
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(libs.bundles.coil)
    implementation(libs.bundles.network)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
}

val apiKey =
    providers
        .fileContents(
            isolated.rootProject.projectDirectory.file("local.properties"),
        ).asText
        .map { text ->
            val properties = Properties()
            properties.load(StringReader(text))
            properties["API_KEY"]
        }.orElse("1234567890")

androidComponents {
    onVariants {
        it.buildConfigFields!!.put(
            "API_KEY",
            apiKey.map { value ->
                BuildConfigField(type = "String", value = """"$value"""", comment = null)
            },
        )
    }
}
