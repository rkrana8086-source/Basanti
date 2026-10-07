plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.voiceassistant"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.voiceassistant"
        minSdk = 24
        targetSdk = 35
        versionCode = 3
        versionName = "1.2"
    }
}
