import com.vanniktech.maven.publish.AndroidSingleVariantLibrary
import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.SourcesJar

plugins {
    id("com.android.library")
    id("com.vanniktech.maven.publish")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.vyvien.permissionpilot.test"
    compileSdk = 36

    defaultConfig {
        minSdk = 23
        consumerProguardFiles("consumer-rules.pro")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    api(project(":permissionpilot-core"))
}

mavenPublishing {
    configure(
        AndroidSingleVariantLibrary(
            variant = "release",
            sourcesJar = SourcesJar.Sources(),
            javadocJar = JavadocJar.Empty(),
        ),
    )

    coordinates(
        groupId = "com.vyvien.permissionpilot",
        artifactId = "permissionpilot-test",
        version = "0.1.0",
    )
    publishToMavenCentral()
    signAllPublications()

    pom {
        name.set("PermissionPilot Test")
        description.set("Test fakes for permission-dependent Android app code.")
    }
}
