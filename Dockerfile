FROM openjdk:17-alpine
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} warehouseProject-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "app.jar"]