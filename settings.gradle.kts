pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "ShowTrakAndroidClient"
include(":app")
include(":showtrak-sdk")
project(":showtrak-sdk").projectDir = file("../ShowTrak-SDK-Android/showtrak-sdk")
