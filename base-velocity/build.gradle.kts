import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.internal.KaptWithoutKotlincTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    kotlin("kapt")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

dependencies {
    compileOnly(libs.bundles.velocity)
    kapt(libs.velocity.api)

    testImplementation(libs.gson)
    testImplementation(libs.guava)
    testImplementation(libs.bundles.database)
    testImplementation(libs.bundles.streamline)
    testImplementation(libs.bundles.kyori)

    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.2")

    compileOnly(project(":base-api"))
    implementation(project(":base-common"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(libs.versions.java.get().toInt())
}

task("sourcesJar", type = Jar::class) {
    from(sourceSets.main.get().allSource)
    archiveClassifier.set("sources")
}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveFileName.set("${project.name}-${project.version}.jar")
        mergeServiceFiles()
        exclude("kotlin/**")
        dependsOn(":base-common:shadowJar")
    }

    named<KotlinCompile>("compileKotlin") {
        dependsOn(":base-common:shadowJar")
    }
}

tasks.withType<KaptWithoutKotlincTask>() {
    dependsOn(":base-common:shadowJar")
}