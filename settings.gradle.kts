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

rootProject.name = "PermissionPilot"

include(
    ":permissionpilot-core",
    ":permissionpilot-compose",
    ":permissionpilot-test",
    ":sample",
)
