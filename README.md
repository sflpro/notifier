# CI Status
[![Build Status](https://travis-ci.org/sflpro/notifier.svg?branch=master)](https://travis-ci.org/sflpro/notifier)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=com.sflpro.notifier%3Anotifier&metric=alert_status)](https://sonarcloud.io/dashboard?id=com.sflpro.notifier%3Anotifier)

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
    <version>1.4.5</version>
</dependency>
```

The API is documented using OpenAPI Specification(Swagger) and the documentation JSON is available under `/swagger.json` 
once the notifier microservice has been started. 

## Deployment

Notifier is dockerized and is easy to deploy as a docker container. For more details, see the images on docker hub:  
https://hub.docker.com/r/sflpro/notifier & https://hub.docker.com/r/sflpro/notifier-worker
