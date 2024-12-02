FROM openjdk:17-jdk-alpine
MAINTAINER alexandruOnel79
COPY target/tema2-0.0.1-SNAPSHOT.jar tema2-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/tema2-0.0.1-SNAPSHOT.jar"]