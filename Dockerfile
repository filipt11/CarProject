FROM eclipse-temurin:17-jdk-jammy
COPY target/CarProject-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
