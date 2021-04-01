plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("kotlin-parcelize")
    `maven-publish`
}

android {
    compileSdkVersion(Versions.compileSdkVersion)
    defaultConfig {
        minSdkVersion(Versions.minSdkVersion)
        targetSdkVersion(Versions.targetSdkVersion)
        val proguardFiles = project.file("proguard").listFiles() ?: emptyArray()
        consumerProguardFiles(*proguardFiles)
    }
    sourceSets {
        configureEach {
            val srcDirs = project.file("src/$name")
                .listFiles { file -> file.isDirectory && file.name.startsWith("res-") }
                ?: emptyArray()
            res.srcDirs(*srcDirs)
        }
    }
    project.getStringExtra("lib.resourcePrefix")?.let {
        resourcePrefix(it)
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
        moduleName = "dev.ebnbin.${project.requireStringExtra("lib.id")}"
    }
    buildFeatures {
        viewBinding = project.getStringExtra("lib.viewBinding")?.toBoolean() ?: false
        dataBinding = project.getStringExtra("lib.dataBinding")?.toBoolean() ?: false
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.0.0-beta03"
    }
}

afterEvaluate {
    publishing {
        publications {
            val publication = project.getStringExtraOrDefault("lib.publication", "release")
            create<MavenPublication>(publication) {
                from(components[publication])
            }
        }
    }
}

//*********************************************************************************************************************

dependencies {
    api(Dependencies.comGithubDevEbnbin_eb.devNotation(project))

    api(Dependencies.androidxLifecycle_lifecycleViewmodelKtx.notation())
    api(Dependencies.androidxAppcompat_appcompat.notation())
//    api(Dependencies.androidxActivity_activityKtx.notation())
    api(Dependencies.androidxFragment_fragmentKtx.notation())
    api(Dependencies.androidxPreference_preferenceKtx.notation())
    api(Dependencies.androidxConstraintlayout_constraintlayout.notation())
    api(Dependencies.androidxCoordinatorlayout_coordinatorlayout.notation())
    api(Dependencies.androidxRecyclerview_recyclerview.notation())
    api(Dependencies.androidxSwiperefreshlayout_swiperefreshlayout.notation())
    api(Dependencies.androidxViewpager2_viewpager2.notation())
    api(Dependencies.androidxCardview_cardview.notation())
    api(Dependencies.androidxGridlayout_gridlayout.notation())
    api(Dependencies.comGoogleAndroidMaterial_material.notation())

    api("androidx.compose.runtime:runtime-livedata:1.0.0-beta03")
    api("androidx.compose.material:material:1.0.0-beta03")
    api("androidx.compose.material:material-icons-extended:1.0.0-beta03")
    api("androidx.lifecycle:lifecycle-viewmodel-compose:1.0.0-alpha03")
    api("androidx.activity:activity-compose:1.3.0-alpha05")
}
