plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.gms.google-services") //Firebase
}

android {
    namespace = "com.clairvoyance.clairvoyance"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.clairvoyance.clairvoyance"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables{
            useSupportLibrary = true
        }
    }

    buildFeatures{
        viewBinding = true
        dataBinding = true
        compose = true
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
    kotlinOptions {
        jvmTarget = "1.8"
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.10"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    configurations {
        all {
            //exclude("androidx.compose.ui")
            exclude("androidx.compose.ui:ui-desktop")
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.compose.ui:ui-graphics-android:1.6.3")
    implementation("androidx.compose.foundation:foundation-android:1.6.3")
    implementation("androidx.compose.compiler:compiler:1.5.10")
    //implementation("androidx.compose.ui:ui-desktop:1.6.3")
    implementation("androidx.compose.material3:material3-android:1.2.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")
    implementation("androidx.compose.runtime:runtime-livedata:1.6.4")
    implementation("androidx.camera:camera-extensions:1.3.2")
    implementation("androidx.camera:camera-view:1.3.2")
    implementation("androidx.compose.material:material-icons-extended-android:1.6.4")
    implementation("com.google.firebase:firebase-auth-ktx:22.3.1")
    implementation("com.google.ar.sceneform:filament-android:1.17.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    debugImplementation("androidx.compose.ui:ui-tooling:1.6.3")

    // Vico
    implementation("com.patrykandpatrick.vico:compose-m3:2.0.0-alpha.14")

    // YCharts
    implementation("co.yml:ycharts:2.1.0")
    
    implementation(platform("com.google.firebase:firebase-bom:32.8.0")) //Firebase
    implementation("com.google.firebase:firebase-auth") //Firebase Authentication
}