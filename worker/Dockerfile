FROM openjdk:11-jre
ARG JAR_FILE
ADD target/${JAR_FILE} /worker.jar
ENTRYPOINT ["java", "-jar", "/worker.jar"]