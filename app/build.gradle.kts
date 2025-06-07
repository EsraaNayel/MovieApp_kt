import com.android.build.gradle.internal.dsl.BaseAppModuleExtension

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)

}
android {
    namespace = "com.esraa.nayel.movieapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.esraa.nayel.movieapp"
        minSdk = 29
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    ndkSetup()

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
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

    implementation(libs.kotlin.serialization)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.paging)
    implementation(libs.androidx.paging.compose)

    implementation(libs.retrofit)
    implementation(libs.retrofit.serialization)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
    implementation(libs.room)
    implementation(libs.room.ktx)
    implementation(libs.room.paging)
    implementation(libs.androidx.room.runtime.android)
    ksp(libs.room.compiler)
    implementation(libs.coil)
    implementation(libs.coil.okhttp)

    debugImplementation(platform(libs.androidx.compose.bom))
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.testing.androidx.compose.manifest)

    testImplementation(libs.testing.junit)
    testImplementation(libs.testing.kotlin)
    testImplementation(libs.testing.kotlin.coroutines)
    testImplementation(libs.mocking.mockito.core)
    testImplementation(libs.mocking.mockito.kotlin)
    testImplementation(libs.asserting.truth)
    testImplementation(platform(libs.androidx.compose.bom))
    testImplementation(libs.testing.compose.junit4)

    androidTestImplementation(libs.testing.androidx.junit)
    androidTestImplementation(libs.testing.androidx.espresso)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.testing.androidx.compose)
}

fun BaseAppModuleExtension.ndkSetup() {
    ndkVersion = "28.0.13004108"
    externalNativeBuild {
        cmake {
            path("src/main/cpp/CMakeLists.txt")
            version = "4.0.2"
        }
    }
}