plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.vyvien.permissionpilot.sample"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.vyvien.permissionpilot.sample"
        minSdk = 23
        targetSdk = 36
        versionCode = 1
        versionName = "0.1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation(project(":permissionpilot-core"))
    implementation(project(":permissionpilot-compose"))

    implementation("androidx.activity:activity-compose:1.9.3")
    implementation("androidx.compose.material3:material3:1.3.1")
    implementation("androidx.compose.ui:ui-tooling-preview:1.7.5")

    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.7.5")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")

    debugImplementation("androidx.compose.ui:ui-tooling:1.7.5")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.7.5")
}
