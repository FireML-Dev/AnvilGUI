plugins {
  `java-library`
  `maven-publish`
}

dependencies {
  api(libs.paper)
  compileOnly(libs.annotations)
}

java {
  toolchain { languageVersion = JavaLanguageVersion.of(21) }
  withJavadocJar()
  withSourcesJar()
}

tasks {
  javadoc {
    options {
      this as StandardJavadocDocletOptions
      val majorVersion =
          libs.versions.paper.get().substringBefore('-').split('.').take(2).joinToString(".")
      links(
          "https://jd.papermc.io/paper/${majorVersion}/",
          "https://jd.advntr.dev/api/${libs.versions.adventure.get()}/",
          "https://javadoc.io/doc/org.jetbrains/annotations/${libs.versions.annotations.get()}/",
      )
    }
  }
}

publishing {
  repositories {
    maven {
      name = "firedevRepo"

      url =
          uri(
              "https://repo.firedev.uk/repository/maven-${
                        if (version.toString().endsWith("-SNAPSHOT")) {
                            "snapshots/"
                        } else {
                            "releases/"
                        }
                    }")

      credentials(PasswordCredentials::class)
      authentication { create<BasicAuthentication>("basic") }
    }
  }
  publications { create<MavenPublication>("maven") { from(components["java"]) } }
}
