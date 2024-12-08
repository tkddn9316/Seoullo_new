pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        maven("https://repository.map.naver.com/archive/maven")
    }
}

rootProject.name = "Seoullo_new"
include(":app")
include(":data")
include(":domain")
