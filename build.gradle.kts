plugins {
	java
	id("org.springframework.boot") version "3.1.4"
	id("io.spring.dependency-management") version "1.1.3"
}

group = "com.example"
version = ""

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("mysql:mysql-connector-java:8.0.33")
	implementation("org.flywaydb:flyway-core:9.22.2")
	implementation("org.flywaydb:flyway-mysql:9.22.2")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testImplementation("org.mockito:mockito-core:5.5.0")
	testImplementation("com.h2database:h2:1.4.200")

}

tasks.withType<Test> {
	useJUnitPlatform()
}
