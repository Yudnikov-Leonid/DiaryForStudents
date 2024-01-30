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
        maven (uri("https://jitpack.io"))
        google()
        mavenCentral()
    }
}

rootProject.name = "DiaryForStudents"
include(":app")
 