FROM openjdk:11-jre
ARG JAR_FILE
ADD target/${JAR_FILE} /api-web.jar
ENTRYPOINT ["java", "-jar", "/api-web.jar"]