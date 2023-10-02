This is an in-progress fork of the original [jgrade](https://github.com/tkutcher/jgrade). A better README forthcoming.
Maven wrapper is used to build the project... `./mvnw clean install` to build the project.
Checkstyle is required for building the project. `./mvnw checkstyle:checkstyle` to run checkstyle.
gotta make sure package version in pom matches version in jGrade.java cli
checkstyle grader uses `maven-checkstyle-plugin`
