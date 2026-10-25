plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.tools.ksp)
}

android {
    namespace = "ru.yandex.buggyweatherapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "ru.yandex.buggyweatherapp"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    // Android core libraries
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.compose.activity)
    implementation(libs.androidx.compose.runtime)
    
    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose.ui)
    implementation(libs.androidx.material3)

    // Other
    implementation(libs.bundles.network)
    implementation(libs.bundles.coroutines)
    implementation(libs.bundles.viewmodel)

    implementation(libs.hilt.android)
    debugImplementation(libs.leakcanary)
    ksp(libs.hilt.compiler)

    implementation(libs.bundles.room)
    ksp(libs.room.compiler)

    // Location
    implementation(libs.play.services.location)

    // Coil image loading
    implementation(libs.coil)


    // Debug only
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


    // Testing
    testImplementation(libs.junit)

    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.ui.test.junit4)
}