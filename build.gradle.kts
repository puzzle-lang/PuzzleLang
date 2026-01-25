import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

plugins {
	alias(libs.plugins.kotlin.multiplatform) apply false
	alias(libs.plugins.kotlin.serialization) apply false
}

subprojects {
	plugins.withId("org.jetbrains.kotlin.multiplatform") {
		extensions.configure<KotlinMultiplatformExtension>("kotlin") {
			sourceSets.all {
				languageSettings {
					enableLanguageFeature("ContextParameters")
					enableLanguageFeature("ExplicitBackingFields")
				}
			}
		}
	}
}