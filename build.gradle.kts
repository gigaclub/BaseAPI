plugins {
    `java-library`
}

group = "net.gigaclub"
version = "14.0.1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    api("org.apache.xmlrpc:xmlrpc-client:3.1.3")
}