plugins {
	alias(libs.plugins.kotlin.multiplatform)
	alias(libs.plugins.kotlin.serialization)
}

group = "puzzle.token"
version = property("puzzle-lang.version").toString()

kotlin {
	jvmToolchain(25)
	
	macosArm64()
	jvm("desktop")
	
	sourceSets {
		commonMain {
			dependencies {
				implementation(libs.bundles.puzzle.token)
				implementation(projects.puzzleContext)
				implementation(projects.puzzleDiagnostic)
				implementation(projects.puzzleBase)
			}
		}
	}
}