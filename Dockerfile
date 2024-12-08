#https://www.docker.com/blog/kickstart-your-spring-boot-application-development/
FROM eclipse-temurin:17-jdk-focal

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
# pe windows docker, nu a fost nevoie de chmod, in schimb pe vm a fost nevoie
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline

COPY src ./src

CMD ["./mvnw", "spring-boot:run"]