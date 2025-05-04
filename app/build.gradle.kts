plugins {
	alias(libs.plugins.android.application)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.kotlin.compose)
	id("com.google.devtools.ksp")
	id("com.google.dagger.hilt.android")
	kotlin("plugin.serialization") version "2.0.21"
}

android {
	namespace = "com.app.infomanager"
	compileSdk = 35
	
	defaultConfig {
		applicationId = "com.app.infomanager"
		minSdk = 26
		targetSdk = 35
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
	kotlinOptions {
		jvmTarget = "11"
	}
	buildFeatures {
		compose = true
	}
}

dependencies {
	
	implementation(libs.androidx.core.ktx)
	implementation(libs.androidx.lifecycle.runtime.ktx)
	implementation(libs.androidx.activity.compose)
	implementation(platform(libs.androidx.compose.bom))
	implementation(libs.androidx.ui)
	implementation(libs.androidx.ui.graphics)
	implementation(libs.androidx.ui.tooling.preview)
	implementation(libs.androidx.material3)
	testImplementation(libs.junit)
	androidTestImplementation(libs.androidx.junit)
	androidTestImplementation(libs.androidx.espresso.core)
	androidTestImplementation(platform(libs.androidx.compose.bom))
	androidTestImplementation(libs.androidx.ui.test.junit4)
	debugImplementation(libs.androidx.ui.tooling)
	debugImplementation(libs.androidx.ui.test.manifest)
	
	implementation("com.google.dagger:hilt-android:2.56.1")
	ksp("com.google.dagger:hilt-android-compiler:2.56.1")
	implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
	
	val room_version = "2.7.0"
	implementation("androidx.room:room-runtime:$room_version")
	implementation("androidx.room:room-ktx:$room_version")
	ksp("androidx.room:room-compiler:$room_version")
	
	implementation("androidx.navigation:navigation-compose:2.8.9")
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
	
	implementation("com.google.mlkit:barcode-scanning:17.3.0")
	implementation("com.google.accompanist:accompanist-permissions:0.37.2")
	val camerax_version = "1.5.0-alpha06"
	implementation("androidx.camera:camera-camera2:${camerax_version}")
	implementation("androidx.camera:camera-lifecycle:${camerax_version}")
	implementation("androidx.camera:camera-view:${camerax_version}")
	implementation("androidx.camera:camera-mlkit-vision:${camerax_version}")
}