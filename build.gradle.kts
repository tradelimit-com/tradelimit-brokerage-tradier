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
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.6.20")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinCoroutineVersion")

    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.20")
    implementation("org.jetbrains.kotlin:kotlin-serialization:1.6.20")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.3.2")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.3.2")

    implementation("io.ktor:ktor-client-resources:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
//    implementation("io.ktor:ktor-client-serialization-jvm:$ktorVersion")
    implementation("io.ktor:ktor-client-logging-jvm:$ktorVersion")
    implementation("io.ktor:ktor-client-cio-jvm:$ktorVersion")


    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$kotlinCoroutineVersion")
    testImplementation("io.ktor:ktor-client-mock:$ktorVersion")
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
