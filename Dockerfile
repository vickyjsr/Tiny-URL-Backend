FROM openjdk:17-jdk-slim as builder
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /app/target/spring-boot-docker.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
