plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android") // Added Kotlin plugin for compatibility
}

android {
    namespace = "com.example.chargingpointsapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.chargingpointsapp"
        minSdk = 21
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.activity:activity:1.7.2")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.android.gms:play-services-maps:18.1.0")
    implementation("com.opencsv:opencsv:4.6") // CSV parsing library
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.8.10") // Kotlin standard library
    implementation ("com.google.android.material:material:1.9.0")
    implementation(libs.junit.junit)
    implementation(libs.core)

    testImplementation("junit:junit:4.13.2")
    testImplementation ("org.mockito:mockito-core:4.3.1")
    implementation ("androidx.recyclerview:recyclerview:1.2.1")
    testImplementation(libs.androidx.junit)
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}