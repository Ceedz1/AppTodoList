plugins {
    alias(libs.plugins.android.application) // You already have this
    id("com.google.gms.google-services")  // Apply the google-services plugin
}

android {
    namespace = "com.example.apptodolistfinals"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.apptodolistfinals"
        minSdk = 24
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // Firebase dependencies
    implementation(platform(libs.firebase.bom))  // Firebase BOM
    implementation(libs.firebase.database)  // Firebase Database
    implementation(libs.firebase.auth)  // Firebase Authentication


    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
