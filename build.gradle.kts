plugins {
    id("java")
    id("org.springframework.boot") version "3.2.0"
    id("io.spring.dependency-management") version "1.1.4"
    id("com.diffplug.spotless") version "6.23.0"
}

group = "io.crossmint"
version = "0.1.0"
java.sourceCompatibility = JavaVersion.VERSION_21

repositories {
    mavenCentral()
}

spotless {
    java {
        target("src/**/*.java")
        googleJavaFormat("1.21.0")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation ("org.springframework.retry:spring-retry")
    implementation ("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.apache.httpcomponents.client5:httpclient5:5.4.4")
    implementation("org.apache.httpcomponents.core5:httpcore5:5.3.6")

    implementation("org.slf4j:slf4j-api:2.0.12")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
