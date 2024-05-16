FROM openjdk:23
COPY build/libs/UserDB-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8086
ENTRYPOINT ["java","-jar","/app.jar"]