import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.tools.ksp)
}

val localProperties = Properties().apply {
    val propertiesFile = rootProject.file("local.properties")
    if (propertiesFile.exists()) {
        load(propertiesFile.inputStream())
    }
}
val weatherApiKey = localProperties.getProperty("WEATHER_API_KEY") ?: ""
val weatherBaseUrl = localProperties.getProperty("WEATHER_BASE_URL") ?: ""

val keystoreProperties = Properties().apply {
    val propertiesFile = rootProject.file("keystore.properties")
    if (propertiesFile.exists()) {
        load(propertiesFile.inputStream())
    }
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

    signingConfigs {
        create("release") {
            storeFile = rootProject.file(keystoreProperties["STORE_FILE"] as String)
            storePassword = keystoreProperties["STORE_PWD"] as String
            keyAlias = keystoreProperties["KEY_ALIAS"] as String
            keyPassword = keystoreProperties["KEY_PWD"] as String
        }
    }

    buildTypes {
        debug {
            buildConfigField("String", "WEATHER_BASE_URL", weatherBaseUrl)
            buildConfigField("String", "WEATHER_API_KEY", weatherApiKey)
        }
        release {
            buildConfigField("String", "WEATHER_BASE_URL", weatherBaseUrl)
            buildConfigField("String", "WEATHER_API_KEY", weatherApiKey)

            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
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
        buildConfig = true
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