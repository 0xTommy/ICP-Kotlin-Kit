// import com.bity.icp_kotlin_kit.file_parser.file_generator.KotlinFileGenerator

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.bity.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.bity.app"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        debug {
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/versions/9/OSGI-INF/MANIFEST.MF"
        }
    }
}

dependencies {
    implementation(project(":icp_kotlin_kit"))
    implementation(libs.coil.compose)
    implementation(libs.koin.androidx.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.tooling.preview)
    implementation(libs.compose.material)
    implementation(libs.navigation.compose)
    implementation(libs.androidx.navigation.runtime)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.kotlinx.serialization.json)
}

/*
tasks.register("parseCandidFiles") {
    val inputFolder = file("./candid_files")
    require(inputFolder.isDirectory)
    inputFolder.listFiles { it -> it.extension == "did" }?.forEach { file ->
        val fileName = file.name.removeSuffix(".did")
        val kotlinFileGenerator = KotlinFileGenerator(
            fileName = fileName,
            packageName = "com.bity.demo_app.generated_files",
            didFileContent = file.readText(Charsets.UTF_8)
        )
        val outputFile = file("./src/main/java/com/bity/app/generated_files/${fileName}.kt")
        outputFile.writeText(kotlinFileGenerator.generateKotlinFile())
    }
}*/
