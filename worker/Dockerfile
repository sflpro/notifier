FROM openjdk:8-jre
ARG JAR_FILE
ADD target/${JAR_FILE} /ms-notifications-consumer.jar
ENTRYPOINT ["java", "-jar", "/ms-notifications-consumer.jar"]