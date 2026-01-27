plugins {
	alias(libs.plugins.kotlin.multiplatform)
	alias(libs.plugins.kotlin.serialization)
}

group = "puzzle.cli"
version = property("puzzle-lang.version").toString()

kotlin {
	jvmToolchain(25)
	
	macosArm64 {
		binaries {
			executable {
				entryPoint = "puzzle.cli.main"
			}
		}
	}
	jvm("desktop")
	
	sourceSets {
		commonMain {
			dependencies {
				implementation(libs.bundles.puzzle.cli)
				implementation(projects.puzzleDriver)
				implementation(projects.puzzleDiagnostic)
				implementation(projects.puzzleCore)
			}
		}
	}
}

tasks.withType<Jar>().configureEach {
	manifest {
		attributes["Main-Class"] = "puzzle.cli.MainKt"
	}
}