import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("kotlin-parcelize")
    `maven-publish`
}

inline fun <reified T> rootProjectExtra(key: String): T {
    return rootProject.extra.properties.getValue(key) as T
}

inline fun <reified T> projectExtra(key: String): T {
    return project.extra.properties.getValue(key) as T
}

fun version(key: String): String {
    return rootProjectExtra<Map<String, String>>("versionMap").getValue(key)
}

fun dependency(id: String): String {
    return "$id:${rootProjectExtra<Map<String, String>>("dependencyMap").getValue(id)}"
}

fun devDependency(id: String): Any {
    return if (gradleLocalProperties(rootDir)["devEnabled"] == "true") {
        project(":$id")
    } else {
        "com.github.ebnbin:$id:${rootProjectExtra<String>("dev.$id")}"
    }
}

android {
    compileSdkVersion(version("compileSdkVersion").toInt())
    defaultConfig {
        minSdkVersion(version("minSdkVersion").toInt())
        targetSdkVersion(version("targetSdkVersion").toInt())
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
    resourcePrefix(projectExtra("resourcePrefix"))
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
        moduleName = "dev.ebnbin.${projectExtra<String>("libId")}"
    }
    buildFeatures {
        viewBinding = projectExtra<String>("viewBinding").toBoolean()
        dataBinding = projectExtra<String>("dataBinding").toBoolean()
    }
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])
            }
        }
    }
}

//*********************************************************************************************************************

dependencies {
    api(devDependency("eb"))

    api(dependency("androidx.lifecycle:lifecycle-viewmodel-ktx"))
    api(dependency("androidx.appcompat:appcompat"))
    api(dependency("androidx.activity:activity-ktx"))
    api(dependency("androidx.fragment:fragment-ktx"))
    api(dependency("androidx.preference:preference-ktx"))
    api(dependency("androidx.constraintlayout:constraintlayout"))
    api(dependency("androidx.coordinatorlayout:coordinatorlayout"))
    api(dependency("androidx.recyclerview:recyclerview"))
    api(dependency("androidx.swiperefreshlayout:swiperefreshlayout"))
    api(dependency("androidx.viewpager2:viewpager2"))
    api(dependency("androidx.cardview:cardview"))
    api(dependency("androidx.gridlayout:gridlayout"))
    api(dependency("com.google.android.material:material"))
}
