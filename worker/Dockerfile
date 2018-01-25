FROM openjdk:8-jre
ARG JAR_FILE
ADD target/${JAR_FILE} /worker.jar
ENTRYPOINT ["java", "-jar", "/worker.jar"]