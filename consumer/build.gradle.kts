plugins {
    id("java")
    id("org.springframework.boot") version "3.2.4"
    id("io.spring.dependency-management") version "1.1.4"
    id("org.openapi.generator") version "7.4.0"
}

group = "com.github.vitaliiev.t1kafka.consumer"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-actuator")
    implementation("org.springframework.kafka:spring-kafka")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")

    compileOnly("org.projectlombok:lombok")
    runtimeOnly("org.postgresql:postgresql")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}
val projectBuildDir = layout.buildDirectory.asFile.get();
val openApiOutputDir = "$projectBuildDir/generated"

sourceSets {
    main {
        java.srcDir("$openApiOutputDir/src/main/java")
    }
}

openApiGenerate {
    generatorName = "spring"
    inputSpec = "$projectDir/src/main/resources/api.yaml"
    outputDir = openApiOutputDir
    apiPackage = "com.github.vitaliiev.t1kafka.consumer.api"
    modelPackage = "com.github.vitaliiev.t1kafka.consumer.model"
    apiFilesConstrainedTo.add("")
    modelFilesConstrainedTo.add("")
    supportingFilesConstrainedTo.add("ApiUtil.java")
    configOptions = mapOf(
        "delegatePattern" to "true",
        "title" to "t1aspect",
        "useJakartaEe" to "true",
        "openApiNullable" to "false",
    )

    validateSpec = true

    typeMappings = mapOf(
        "OffsetDateTime" to "java.time.LocalDateTime",
    )
}

tasks.compileJava {
    dependsOn("openApiGenerate")
}