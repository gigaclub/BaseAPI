plugins {
    `java-library`
    `maven-publish`
}

group = "net.gigaclub"
version = "14.0.1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    api("org.apache.xmlrpc:xmlrpc-client:3.1.3")
}

tasks {
    val sourcesJar by creating(Jar::class) {
        archiveClassifier.set("sources")
        from(sourceSets.main.get().allSource)
    }

    val javadocJar by creating(Jar::class) {
        dependsOn.add(javadoc)
        archiveClassifier.set("javadoc")
        from(javadoc)
    }

    artifacts {
        archives(sourcesJar)
        archives(javadocJar)
        archives(jar)
    }
}