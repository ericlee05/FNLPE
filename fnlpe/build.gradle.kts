plugins {
    java
    kotlin("jvm") version "1.4.10"
}

group = "org.github.vinto1819"
version = "1.0-BETA"

repositories {
    mavenCentral()
    maven(url="https://jitpack.io")
}

dependencies {
    implementation(kotlin("stdlib"))
    testCompile("junit", "junit", "4.12")

    implementation("com.github.shin285:KOMORAN:3.3.4")
}
