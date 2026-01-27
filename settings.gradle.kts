rootProject.name = "puzzle-lang"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
	repositories {
		google()
		gradlePluginPortal()
		mavenCentral()
	}
}

plugins {
	id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

dependencyResolutionManagement {
	@Suppress("UnstableApiUsage")
	repositories {
		google()
		mavenCentral()
	}
}

include(":puzzle-cli")
include(":puzzle-driver")
include(":puzzle-frontend")
include(":puzzle-sema")
include(":puzzle-backup")
include(":puzzle-diagnostic")
include(":puzzle-builtin")
include(":puzzle-export")
include(":puzzle-core")

include(":puzzle-config")
include(":puzzle-token")
include(":puzzle-ast")
include(":puzzle-symbol")
include(":puzzle-ir")