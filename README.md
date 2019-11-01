[![Build Status](https://travis-ci.org/sflpro/notifier.svg?branch=master)](https://travis-ci.org/sflpro/notifier)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=com.sflpro.notifier%3Anotifier&metric=alert_status)](https://sonarcloud.io/dashboard?id=com.sflpro.notifier%3Anotifier)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.sflpro.notifier/notifier/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.sflpro.notifier/notifier/)
[![License: APACHE](https://img.shields.io/badge/license-Apache%20License%202.0-b)](https://opensource.org/licenses/Apache-2.0)


# Notifications Microservice
The notifications microservice is an independently deployable component intended for sending various types of notifications. 
It provides a common abstraction layer on top of the various notification types and actual sending, tracking routines.

## Supported notification types
Notifier supports the most common notification types and platforms. The project aims to enable the user to only 
rely on notifier for client messaging/communication. Below are the supported notification types and implementations.

#### Email Notifications

The microservice supports sending regular emails. The supported transports are:
* SMTP
* MailChimp / [Mandril](https://mandrill.com/)

#### SMS Notifications

The microservice supports sending SMS notifications. The supported transports are:
* [Twilio](https://www.twilio.com/) 

#### Push Notifications

Push notifications to mobile devices are supported. As of now, only one provider is available: 
* [Amazon SNS](https://aws.amazon.com/sns/)

## Integration

You can call the notifier's REST API directly or use the provided Java client library. To use the client library add the 
following maven dependency:
```xml
<dependency>
    <groupId>com.sflpro.notifier</groupId>
    <artifactId>api-client-library</artifactId>
    <version>1.7.0</version>
</dependency>
```

The API is documented using OpenAPI Specification(Swagger) and the documentation JSON is available under `/swagger.json` 
once the notifier microservice has been started. 

## Deployment

Notifier is dockerized and is easy to deploy as a docker container. For more details, see the images on docker hub:  
https://hub.docker.com/r/sflpro/notifier & https://hub.docker.com/r/sflpro/notifier-worker

## Running the application locally

#### 1. Configure POSTGRES datasource
The postgres has official images in [docker hub](https://hub.docker.com/_/postgres).
```bash
docker run --name notifier-postgres -e POSTGRES_PASSWORD=notifier -e POSTGRES_USER=notifier -e POSTGRES_DB=notifier -p 5432:5432 -d postgres:11
```

#### 2. Configure RabbitMQ queue engine
RabbitMQ has official images in [docker hub](https://hub.docker.com/_/rabbitmq).
Running RabbitMQ with [management plugin](https://www.rabbitmq.com/management.html) enabled
```bash
docker run -d --hostname notifier-rabbit --name some-rabbit -p 5671:5671 -p 5672:5672 -p 15672:15672 rabbitmq:3-management
```

#### 3. Prepare properties file for notifier images
```properties
# Postgres
spring.datasource.url=jdbc:postgresql://{HOST_IP}:5432/POSTGRES_DB
spring.datasource.username=POSTGRES_USER
spring.datasource.password=POSTGRES_PASSWORD

# Notifier queue engine
notifier.queue.engine=rabbit

# RabbitMQ
spring.rabbitmq.host={HOST_IP}
spring.rabbitmq.username=guest

# Container port
server.port=8080
```

#### 4. Run notifier-api
Running [Notifier-API](https://hub.docker.com/r/sflpro/notifier-api) docker images.
```bash
docker run -p 8080:8080 -v {NOTIFIER_-_PROPERTIES_PATH}:/etc/notifier/notifier.properties sflpro/notifier-api:1.7.0 --spring.config.additional-location=etc/notifier/notifier.properties
```
Running [Notifier-Worker](https://hub.docker.com/r/sflpro/notifier-worker) docker images
```bash
docker run -p 8081:8080 -v {NOTIFIER_-_PROPERTIES_PATH}:/etc/notifier/notifier.properties sflpro/notifier-worker:1.7.0 --spring.config.additional-location=etc/notifier/notifier.properties
```

#### 5. Testing setup
```curl
curl -X POST "localhost:8080/notification/email/create"  -H "accept: application/json" -H "Content-Type: application/json" -d "{\"recipientEmail\":\"recipient-email@example.com\",\"senderEmail\":\"sender-email@example.com\",\"subject\":\"testing-subject\",\"templateName\":\"Email template name\"}"
```