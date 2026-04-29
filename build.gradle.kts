plugins {
    java
    id("org.springframework.boot") version "4.0.6"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.backend.course"
version = "0.0.1-SNAPSHOT"
description = "backend-course-additional"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // Web
    implementation("org.springframework.boot:spring-boot-starter-web")

    // Swagger UI
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")

    // Lombok (только для компиляции)
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
