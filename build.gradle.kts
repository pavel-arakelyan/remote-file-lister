plugins {
    kotlin("jvm") version "1.9.23"
    id("org.jetbrains.compose") version "1.8.0-alpha01"
    application
}

group = "com.tools"
version = "1.0-SNAPSHOT"

val jdkVersion = "21"

repositories {
    mavenCentral()
    google()
    maven("https://packages.jetbrains.team/maven/p/kpm/public/")
}

dependencies {
    implementation("org.jetbrains.jewel:jewel-int-ui-standalone-243:0.27.0")
    implementation("org.jetbrains.jewel:jewel-int-ui-decorated-window-243:0.27.0")
    // Do not bring in Material (we use Jewel)
    implementation(compose.desktop.currentOs) {
        exclude(group = "org.jetbrains.compose.material")
    }
    implementation(compose.components.resources)
    implementation("org.jetbrains.compose.compiler:compiler:1.5.13.5")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.named<JavaExec>("run") {
    systemProperty("dir", System.getProperty("dir"))
}

tasks {
    withType<JavaExec> {
        // afterEvaluate is needed because the Compose Gradle Plugin
        // register the task in the afterEvaluate block
        afterEvaluate {
            javaLauncher = project.javaToolchains.launcherFor { languageVersion = JavaLanguageVersion.of(jdkVersion) }
            setExecutable(javaLauncher.map { it.executablePath.asFile.absolutePath }.get())
        }
    }
}

kotlin {
    jvmToolchain(jdkVersion.toInt())
}

application {
    mainClass.set("com.tools.remotefilelister.MainKt")
}