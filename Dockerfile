FROM eclipse-temurin:17-jdk-jammy AS builder
WORKDIR /CarProject
COPY . .
RUN ./mvnw clean package -DskipTests
FROM eclipse-temurin:17-jre-jammy
WORKDIR /CarProject
COPY --from=builder /CarProject/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
