plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    `maven-publish`
}

android {
    val versionMap: Map<String, String> by rootProject.extra
    compileSdkVersion(versionMap.getValue("compileSdkVersion").toInt())
    defaultConfig {
        minSdkVersion(versionMap.getValue("minSdkVersion").toInt())
        targetSdkVersion(versionMap.getValue("targetSdkVersion").toInt())
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
    resourcePrefix("ebui_")
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
        moduleName = "dev.ebnbin.ebui"
    }
}

dependencies {
    val dependencyMap: Map<String, String> by rootProject.extra
    fun dependency(id: String): String {
        val version = dependencyMap[id].also {
            requireNotNull(it)
        }
        return "$id:$version"
    }

    fun devDependency(id: String): Any {
        return if (rootProject.extra.has("devLib.$id")) {
            "com.github.ebnbin:$id:${rootProject.extra["devLib.$id"]}"
        } else {
            project(":$id")
        }
    }

    api(devDependency("eb"))

    api(dependency("androidx.recyclerview:recyclerview"))
    api(dependency("com.google.android.material:material"))
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
