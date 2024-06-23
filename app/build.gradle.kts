plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.ksp)
}

android {
    namespace = "ru.nikolas_snek.tfsspring2024"
    compileSdk = 34

    defaultConfig {
        applicationId = "ru.nikolas_snek.tfsspring2024"
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(project(":features:chat"))
    implementation(project(":features:channels"))
    implementation(project(":features:peoples"))
    implementation(project(":features:another-people"))
    implementation(project(":features:profile"))
    implementation(project(":core:ui_kit"))
    implementation(project(":core:navigation"))
    implementation(project(":data"))
    implementation(project(":core:data_di"))
    implementation(project(":core:mvi"))
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.cicerone)
    implementation(libs.viewbindingpropertydelegate.noreflection)
    implementation(libs.kotlinx.coroutines.core)
    // Dagger 
    implementation(libs.dagger)
    ksp(libs.dagger.compiler)
    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.moshi)
    implementation(libs.converter.moshi)
    ksp(libs.moshi.kotlin.codegen)
    implementation(libs.logging.interceptor)

    // room
    implementation(libs.androidx.room.runtime)
    annotationProcessor(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Test
    androidTestImplementation(libs.kaspresso)
    androidTestImplementation(libs.kaspresso.allure.support)
    androidTestImplementation(libs.androidx.fragment.testing)
    androidTestImplementation(libs.androidx.junit.ktx)
    androidTestImplementation(libs.mockito.android)
    testImplementation(libs.mockito.inline)
    testImplementation(libs.byte.buddy.agent)
    androidTestImplementation(libs.mockwebserver)
    androidTestImplementation(libs.dagger)
    kspAndroidTest(libs.dagger.compiler)
}