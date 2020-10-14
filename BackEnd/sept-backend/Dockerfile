# syntax=docker/dockerfile:experimental
#
# Build stage
#
FROM maven:3.6.0-jdk-11-slim AS build
COPY pom.xml /usr/app/
COPY src /usr/app/src
WORKDIR /usr/app
RUN --mount=type=cache,target=/root/.m2 mvn -B package
#
# Package stage
#
FROM openjdk:11-jre-slim
COPY --from=build /usr/app/target/*.jar /usr/app/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/app/app.jar", "--spring.profiles.active=deploy"]