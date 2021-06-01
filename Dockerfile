FROM openjdk:11

EXPOSE 8080

COPY build/libs/messages-api-rest-g-0.0.1-SNAPSHOT.jar /app.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
