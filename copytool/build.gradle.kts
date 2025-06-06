plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("maven-publish")
}

android {
    namespace = "com.alexius.copytool"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation("io.github.java-diff-utils:java-diff-utils:4.9")
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])

                groupId = "com.github.alexiyous"
                artifactId = "kopytool"
                version = "1.0.0"

                pom {
                    name.set("KopyTool")
                    description.set("Android Copy Tool Library")
                    url.set("https://github.com/alexiyous/kopy-tool")

                    licenses {
                        license {
                            name.set("Apache License 2.0")
                            url.set("https://www.apache.org/licenses/LICENSE-2.0")
                        }
                    }

                    developers {
                        developer {
                            id.set("alexiyous")
                            name.set("Alexius Andrianno Alfa Renanta")
                            email.set("alexiusrenanta@gmail.com")
                        }
                    }

                    scm {
                        connection.set("scm:git:github.com/alexiyous/kopy-tool.git")
                        developerConnection.set("scm:git:ssh://github.com/alexiyous/kopy-tool.git")
                        url.set("https://github.com/alexiyous/kopy-tool/tree/main")
                    }
                }
            }
        }
    }
}