plugins {
	alias(libs.plugins.kotlin.multiplatform)
	alias(libs.plugins.kotlin.serialization)
}

group = "puzzle.backup"
version = property("puzzle-lang.version").toString()

kotlin {
	jvmToolchain(25)
	
	macosArm64()
	jvm("desktop")
	
	sourceSets {
		commonMain {
			dependencies {
				implementation(libs.bundles.puzzle.backup)
			}
		}
	}
}