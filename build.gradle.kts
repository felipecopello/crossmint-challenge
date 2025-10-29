plugins {
    id("java")
    id("jacoco")
    id("org.springframework.boot") version "3.2.0"
    id("io.spring.dependency-management") version "1.1.4"
    id("com.diffplug.spotless") version "6.23.0"
}

jacoco {
    toolVersion = "0.8.12" // Use latest stable version
}

tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests must run before generating report

    reports {
        xml.required.set(true)  // generate XML report (good for CI tools)
        csv.required.set(false)
        html.required.set(true) // generate HTML report
        html.outputLocation.set(layout.buildDirectory.dir("reports/jacoco"))
    }
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
        removeUnusedImports()
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

    compileOnly("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")

    testCompileOnly("org.projectlombok:lombok:1.18.34")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.34")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation ("org.mockito:mockito-core:5.4.0")
    testImplementation ("org.mockito:mockito-junit-jupiter:5.4.0")
}

tasks.withType<Test> {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.register("runApp") {
    dependsOn("spotlessCheck", "test")
    finalizedBy("bootRun")
}
