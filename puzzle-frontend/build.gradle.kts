plugins {
	alias(libs.plugins.kotlin.multiplatform)
	alias(libs.plugins.kotlin.serialization)
}

group = "puzzle.frontend"
version = property("puzzle-lang.version").toString()

kotlin {
	jvmToolchain(25)
	
	macosArm64()
	jvm("desktop")
	
	sourceSets {
		commonMain {
			dependencies {
				implementation(libs.bundles.puzzle.frontend)
				implementation(projects.puzzleContext)
				implementation(projects.puzzleToken)
				implementation(projects.puzzleAst)
				implementation(projects.puzzleDiagnostic)
				implementation(projects.puzzleBase)
			}
		}
	}
}