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
    testOptions.unitTests.isIncludeAndroidResources = true
    buildFeatures {
        buildConfig = true
    }
}

spotless {
    kotlin {
        target("src/**/*.kt")
        ktlint()
    }
    kotlinGradle {
        target("**/*.gradle.kts")
        ktlint()
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(libs.bundles.coil)
    implementation(libs.bundles.network)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
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
