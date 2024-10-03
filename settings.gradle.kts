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

rootProject.name = "My Spotify"
include(":app")
include(":core")
include(":core:data")
include(":core:presentation")
include(":core:domain")
include(":spotify")
include(":spotify:data")
include(":spotify:domain")
include(":spotify:auth")
include(":spotify:presentation")
include(":spotify:apicontract")
