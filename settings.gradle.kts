pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
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

rootProject.name = "TFS spring 2024"
include(":app")
include(":homework_1")
include(":features:chat")
include(":features:channels")
include(":features:peoples")
include(":features:another-people")
include(":features:profile")
include(":core:ui_kit")
include(":data")
include(":core:navigation")
include(":core:data_di")
include(":core:mvi")
