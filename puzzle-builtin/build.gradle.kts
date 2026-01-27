plugins {
	alias(libs.plugins.kotlin.multiplatform)
	alias(libs.plugins.kotlin.serialization)
}

group = "puzzle.builtin"
version = property("puzzle-lang.version").toString()

kotlin {
	jvmToolchain(25)
	
	macosArm64()
	jvm("desktop")
	
	sourceSets {
		commonMain {
			dependencies {
				implementation(libs.bundles.puzzle.builtin)
				implementation(projects.puzzleSema)
				implementation(projects.puzzleAst)
				implementation(projects.puzzleToken)
				implementation(projects.puzzleCore)
			}
		}
	}
}