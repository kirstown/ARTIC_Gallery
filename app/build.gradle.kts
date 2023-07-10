plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "dev.kirstenbaker.gallery"
    compileSdk = 34

    defaultConfig {
        applicationId = "dev.kirstenbaker.gallery"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    kotlinOptions {
        // targeting 17 due to kapt issues
        jvmTarget = "17"
    }
    compileOptions {
        // targeting 17 due to kapt issues
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes.add("META-INF/*")
        }
    }
}

dependencies {
    implementation(libs.androidx.benchmark.common)
    implementation(libs.androidx.paging.testing)
    implementation(libs.mockk)
    implementation(libs.coil)
    implementation(libs.coil.compose)
    // Including this due to the issues with upgrading Kotlin (see kapt note below)
    // https://youtrack.jetbrains.com/issue/KT-54136/Duplicated-classes-cause-build-failure-if-a-dependency-to-kotlin-stdlib-specified-in-an-android-project
    constraints {
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.8.0") {
            because("kotlin-stdlib-jdk7 is now a part of kotlin-stdlib")
        }
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.0") {
            because("kotlin-stdlib-jdk8 is now a part of kotlin-stdlib")
        }
    }
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ui.test.junit4)
    androidTestImplementation(platform(libs.compose.bom))
    debugImplementation(libs.leakcanary)
    debugImplementation(libs.ui.test.manifest)
    debugImplementation(libs.ui.tooling)
    implementation(libs.activity.compose)
    implementation(libs.converter.moshi)
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.compose)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.material)
    implementation(libs.material3)
    implementation(libs.moshi)
    implementation(libs.navigation.compose)
    implementation(libs.retrofit)
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(platform(libs.compose.bom))
    implementation(libs.hilt.android)
    implementation(libs.androidx.paging.runtime.ktx)
    implementation(libs.androidx.paging.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    kapt(libs.hilt.compiler)
    // using kapt rather than ksp due to versioning bug: https://github.com/google/ksp/issues/1288
    kapt(libs.moshi.kotlin.codegen)
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
}

kapt {
    correctErrorTypes = true
}