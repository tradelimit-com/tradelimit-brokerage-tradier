plugins {
    idea
    kotlin("jvm")
    kotlin("plugin.serialization")
}

group = "org.tradelimit"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven {
        url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap")
        name = "ktor-eap"
    }
}

val kotlinVersion: String by project
val ktorVersion: String by project
val kotlinCoroutineVersion: String by project


dependencies {
    implementation(kotlin("stdlib"))

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinCoroutineVersion")

    implementation(kotlin("gradle-plugin", version = kotlinVersion))
    implementation(kotlin("serialization", version = kotlinVersion))

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.3.2")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.3.2")

    implementation("io.ktor:ktor-client-serialization-jvm:$ktorVersion")
    implementation("io.ktor:ktor-client-logging-jvm:$ktorVersion")
    implementation("io.ktor:ktor-client-cio-jvm:$ktorVersion")


    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$kotlinCoroutineVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }

    test {
        useJUnitPlatform()
        maxHeapSize = "1G"
    }
}
