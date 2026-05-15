plugins {
    alias(libs.plugins.com.android.application)
}

android {
    namespace = "ir.ammari.nodelook"
    compileSdk = 37

    defaultConfig {
        applicationId = "ir.ammari.nodelook"
        minSdk = 1
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

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
