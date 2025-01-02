FROM openjdk:17
ARG JAR_FILE=build/libs/karrot-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} /app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]