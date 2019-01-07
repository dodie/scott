# Prerequisites

- Import the keys:
  - `gpg2 --list-keys`
    - Observe that there are no keys imported.
  - `gpg2 --import private-keys` and `gpg2 --import pub-keys`
  - `gpg2 --list-keys`
    - Observe that the keys are imported.
- Create a new `servers/server` entry in the `.m2/settings.xml` file with `nexus-releases` as an id. The specify the `username` and `password` for the `oss.sonatype.org` Nexus user.


# Release

- Bump version.
- Build everything:
  - Run `mvn clean install` in the root of the project.
  - Run`./gradlew build` on the `scott-gradle-plugin`.
- Deploy Maven Artifacts:
  - For `scott/scott` and `scott/scott-maven-plugin` do the following: (with the appropriate version numbers):
    - `mvn gpg:sign-and-deploy-file -Durl=https://oss.sonatype.org/service/local/staging/deploy/maven2/ -DrepositoryId=nexus-releases -DpomFile=pom.xml -Dfile=target/scott-1.0.0.jar`
    - `mvn gpg:sign-and-deploy-file -Durl=https://oss.sonatype.org/service/local/staging/deploy/maven2/ -DrepositoryId=nexus-releases -DpomFile=pom.xml -Dfile=target/scott-1.0.0-sources.jar -Dclassifier=sources`
    - `mvn gpg:sign-and-deploy-file -Durl=https://oss.sonatype.org/service/local/staging/deploy/maven2/ -DrepositoryId=nexus-releases -DpomFile=pom.xml -Dfile=target/scott-1.0.0-javadoc.jar -Dclassifier=javadoc`
   - Sign in to [https://oss.sonatype.org](https://oss.sonatype.org). Find the new repo under "Staging repositories". Make sure that the contents are OK, then hit Close, then Release.
- Deploy `scott/scott-gradle-plugin`:
  - `./gradlew publishPlugins`


The Gradle plugin will be released after manual approval. The Maven artifacts will be available in Maven Central after a few hours.

