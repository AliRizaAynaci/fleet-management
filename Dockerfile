FROM openjdk:17-jdk-slim AS build

COPY pom.xml mvnw ./
COPY .mvn .mvn
RUN ./mvnw dependency:resolve

COPY src src
RUN ./mvnw package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /fleetmanagement
COPY --from=build target/*.jar fleetmanagement.jar

ENTRYPOINT ["java", "-jar", "fleetmanagement.jar"]
