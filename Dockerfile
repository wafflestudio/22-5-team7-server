# FROM openjdk:17
# ARG JAR_FILE=build/libs/karrot-0.0.1-SNAPSHOT.jar
# COPY ${JAR_FILE} /app.jar
# EXPOSE 8080
# ENTRYPOINT ["java", "-jar", "/app.jar"]

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
ARG JAR_FILE=build/libs/karrot-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-Xmx512m", "-Xms256m", "-jar", "app.jar"]
EXPOSE 8080