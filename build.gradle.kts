plugins {
    kotlin("jvm") version "2.2.20"
    kotlin("plugin.serialization") version "2.2.20"

}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.1")
    implementation("com.github.doyaaaaaken:kotlin-csv-jvm:1.9.1")
    implementation ("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.17.0")
    implementation ("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.0")
}
tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}