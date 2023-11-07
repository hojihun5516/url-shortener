import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.1.4"
	id("io.spring.dependency-management") version "1.1.3"
	id("jacoco")
	kotlin("jvm") version "1.8.22"
	kotlin("plugin.spring") version "1.8.22"
	kotlin("plugin.jpa") version "1.8.22"
	kotlin("kapt") version "1.8.22"
}

group = "com.moflow"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("io.github.oshai:kotlin-logging-jvm:5.0.1")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")
	implementation("org.springframework.boot:spring-boot-starter-data-redis")
	implementation("com.h2database:h2:2.2.224") // docker로 mysql 띄우기 전 임시 db
	implementation("org.springframework.boot:spring-boot-starter-validation")
	kapt("com.querydsl:querydsl-apt:5.0.0:jakarta")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
	testImplementation("com.appmattus.fixture:fixture:1.2.0")
	testImplementation("com.ninja-squad:springmockk:4.0.2")
	testImplementation("io.mockk:mockk:1.13.2")
	testRuntimeOnly("com.h2database:h2")
}
jacoco {
	toolVersion = "0.8.11"
}
tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

allOpen {
	annotation("jakarta.persistence.Entity")
	annotation("jakarta.persistence.Embeddable")
	annotation("jakarta.persistence.MappedSuperclass")
}

tasks.withType<Test> {
	useJUnitPlatform()
	finalizedBy("jacocoTestReport")
}


tasks.jacocoTestReport {
	reports {
		html.required.set(true)
		xml.required.set(true)
		csv.required.set(false)
	}

	classDirectories.setFrom(
		sourceSets.main.get().output.asFileTree.matching {
			exclude("**/UrlShortenerApplication.kt*")
		}
	)

	finalizedBy("jacocoTestCoverageVerification")
}

tasks.jacocoTestCoverageVerification {
	violationRules {
		rule {
			enabled = true
			element = "CLASS"

			limit {
				counter = "LINE"
				value = "COVEREDRATIO"
				minimum = 0.0.toBigDecimal()
			}
		}
	}
}

val testCoverage by tasks.registering {
	group = "verification"
	description = "Runs the unit tests with coverage"

	dependsOn(
		":test",
		":jacocoTestReport",
		":jacocoTestCoverageVerification",
	)

	tasks["jacocoTestReport"].mustRunAfter(tasks["test"])
	tasks["jacocoTestCoverageVerification"].mustRunAfter(tasks["jacocoTestReport"])
}
