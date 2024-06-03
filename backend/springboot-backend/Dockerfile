FROM eclipse-temurin:17-jdk-jammy as builder
WORKDIR /opt/app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline
COPY ./src ./src
RUN ./mvnw clean install -Dspring.profiles.active=docker 

# FROM openjdk:17-jdk-alpine
# FROM eclipse-temurin
FROM eclipse-temurin:17-jre-jammy
WORKDIR /opt/app
# ARG src="target/springboot-backend-3.3.0.jar"
# COPY ${src} springboot-backend-3.3.0.jar
COPY --from=builder /opt/app/target/*.jar /opt/app/*.jar
COPY rsa* /opt/app/
EXPOSE 8088
ENTRYPOINT ["java","-jar","/opt/app/*.jar"]


# FROM maven:3.8.4-openjdk-17 as maven-builder
# COPY src /app/src
# COPY pom.xml /app
# RUN mvn -f /app/pom.xml clean package -DskipTests
# FROM openjdk:17-alpine
# COPY --from=maven-builder app/target/springboot-backend-3.3.0.jar /app-service/springboot-backend-3.3.0.jar
# WORKDIR /app-service
# EXPOSE 8088
# ENTRYPOINT ["java","-jar","springboot-backend-3.3.0.jar"]
