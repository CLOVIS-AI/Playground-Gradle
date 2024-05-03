plugins {
	alias(opensavvyConventions.plugins.base)
	alias(opensavvyConventions.plugins.kotlin.library)
}

kotlin {
	jvm()
	js {
		browser()
		nodejs()
	}
	linuxX64()
	iosArm64()
	iosSimulatorArm64()
	iosX64()

	sourceSets.commonTest.dependencies {
		// TODO after https://gitlab.com/opensavvy/automation/gradle-conventions/-/merge_requests/44
		// replace by a proper version catalog access
		implementation("org.jetbrains.kotlin:kotlin-test:${opensavvyConventions.versions.kotlin.get()}")
	}

	sourceSets.jvmTest.dependencies {
		implementation(opensavvyConventions.aligned.kotlin.test.junit5)
	}
}

library {
	name.set("Playground Core")
	description.set("Project template with configured MavenCentral publication")
	homeUrl.set("https://gitlab.com/opensavvy/playgrounds/gradle")

	license.set {
		name.set("Apache 2.0")
		url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
	}
}

// region Publication test
// Even though this module is included in all repositories that import the Playground, we
// don't want to always publish this template.

val appGroup: String? by project

if (appGroup != "dev.opensavvy.playground") {
	tasks.configureEach {
		if (name.startsWith("publish")) {
			onlyIf("Publishing is only enabled when built as part of the Playground") { false }
		}

		if (this is Test) {
			onlyIf("The template tests do not need to run when not building as part of the Playground") { System.getenv("CI") != null }
		}
	}
}

// endregion
