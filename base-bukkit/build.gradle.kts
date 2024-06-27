import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.internal.KaptWithoutKotlincTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

dependencies {
    compileOnly(libs.bundles.paper)
    compileOnly(project(":base-api"))
    implementation(project(":base-common"))
    implementation(project(":base-bukkit-api"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.shadowJar {
    manifest {
        attributes["paperweight-mappings-namespace"] = "mojang"
    }
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