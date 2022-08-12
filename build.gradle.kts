plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "jp.iceserver"
version = "1.0.0"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
    maven ("https://papermc.io/repo/repository/maven-public/")
    maven  ("https://oss.sonatype.org/content/groups/public/")
    maven ("https://jitpack.io")
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation("com.github.hazae41", "mc-kutils", "master-SNAPSHOT")

    compileOnly("io.papermc.paper:paper-api:1.18.2-R0.1-SNAPSHOT")
}

val jar by tasks.getting(Jar::class) {
    manifest {
        attributes["Main-Class"] = "jp.iceserver.icecat.IceCatKt"
    }

    from(
        configurations.compileClasspath.get().map {
            if (it.isDirectory) it else zipTree(it)
        }
    )
    exclude("META-INF/*.RSA", "META-INF/*.SF", "META-INF/*.DSA")

    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}