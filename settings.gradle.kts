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
    versionCatalogs {
        create("tegralLibs") {
            from("guru.zoroark.tegral:tegral-catalog:0.0.4")
        }
    }
}

rootProject.name = "ICP Kotlin Kit"
include(":icp_kotlin_kit")
include(":demo_app")
