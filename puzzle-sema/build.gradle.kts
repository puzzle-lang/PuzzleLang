plugins {
	alias(libs.plugins.kotlin.multiplatform)
	alias(libs.plugins.kotlin.serialization)
}

group = "puzzle.sema"
version = property("puzzle-lang.version").toString()

kotlin {
	jvmToolchain(25)
	
	macosArm64()
	jvm("desktop")
	
	sourceSets {
		commonMain {
			dependencies {
				implementation(libs.bundles.puzzle.sema)
				implementation(projects.puzzleSymbol)
				implementation(projects.puzzleToken)
				implementation(projects.puzzleAst)
				implementation(projects.puzzleConfig)
				implementation(projects.puzzleCore)
			}
		}
	}
}