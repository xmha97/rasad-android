import org.gradle.kotlin.dsl.lint
import kotlin.plus

plugins {
    alias(libs.plugins.com.android.application)
}

android {
    namespace = "ir.ammari.rasad"
    compileSdk = 36

    defaultConfig {
        applicationId = "ir.ammari.rasad"
        minSdk = 1
        targetSdk = 36
        versionCode = 1
        versionName = "0.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables.generatedDensities()
    }

    // https://stackoverflow.com/a/75544119
    packaging { dex { useLegacyPackaging = true } }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"))
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs["debug"]
        }
    }

    buildFeatures { resValues = false }


    lint {
        warningsAsErrors = true
        abortOnError = true
        checkAllWarnings = true
        checkReleaseBuilds = true
        checkDependencies = true
        checkTestSources = true
        checkGeneratedSources = true
        baseline = file("lint-baseline.xml") // To update: ./gradlew updateLintBaseline
    }
}

dependencies {
}
