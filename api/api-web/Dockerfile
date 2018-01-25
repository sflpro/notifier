FROM openjdk:8-jre
ARG JAR_FILE
ADD target/${JAR_FILE} /api-web.jar
ENTRYPOINT ["java", "-jar", "/api-web.jar"]