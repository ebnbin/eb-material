buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(Dependencies.comAndroidToolsBuild_gradle.composeNotation())
        classpath(Dependencies.orgJetbrainsKotlin_kotlinGradlePlugin.notation())
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        jcenter()
        maven("https://jitpack.io")
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}
