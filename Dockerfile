FROM openjdk:8-jdk-alpine
VOLUME /tmp
EXPOSE 8080
ADD build/libs/Mini-Url-1.0.jar app.jar
ENTRYPOINT [ "sh", "-c", "java -jar /app.jar" ]