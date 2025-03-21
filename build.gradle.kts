plugins {
    kotlin("jvm") version "2.1.10"
}

group = "org.example"
version = ""

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.knowm.xchart:xchart:3.8.0")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}
tasks.jar {
    manifest {
        attributes(
            "Main-Class" to "org.example.MainKt"
        )
    }
}