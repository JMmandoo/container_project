FROM openjdk:17-alpine
ARG JAR_FILE=*.jar
COPY ./build/libs/warehouseProject-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]