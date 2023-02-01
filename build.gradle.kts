import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = "com.peanut.pc.nas"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}


kotlin {
    jvm {
        jvmToolchain(11)
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)
                implementation("net.harawata:appdirs:1.2.1")
                implementation("com.alibaba:fastjson:2.0.23")
                implementation("com.squareup.okhttp3:okhttp:4.10.0")
                implementation("com.squareup.retrofit2:retrofit:2.9.0")
                implementation("com.squareup.retrofit2:converter-gson:2.0.2")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation("net.harawata:appdirs:1.2.1")
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        //./gradlew :desktop:packageDistributionForCurrentOS
        //# outputs are written to desktop/build/compose/binaries
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "花生酱的NAS"
            packageVersion = "1.0.0"
            description = "NAS的PC客户端，适用与Windows、Linux与MacOS"
            copyright = "© 2023 PeanutButter. All rights reserved."
            macOS {
                iconFile.set(project.file("icon.png"))
            }
            windows {
                iconFile.set(project.file("logo.ico"))
            }
            linux {
                iconFile.set(project.file("logo.png"))
            }
        }
    }
}
