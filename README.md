# CI Status
[![Build Status](https://travis-ci.org/sflpro/notifier.svg?branch=master)](https://travis-ci.org/sflpro/notifier)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=com.sflpro.notifier%3Anotifier&metric=alert_status)](https://sonarcloud.io/dashboard?id=com.sflpro.notifier%3Anotifier)

# Notifications Microservice
The notifications microservice is a separataely deployable component intended for sending various types of notifications. It provides a common abstraction layer on top of the various notification types and actual sending, tracking routines.
Currently microservice supports :
* Email notifications
* SMS notifications
* Push notifications

## Supported notification types
Notifier supports a number of notification types and platforms. The project aim to enable the user to only rely on 
notifier for client messaging/communication. 
Bellow are the supported notification types and implementations.

#### Email Notifications

The microservice supports sending regular emails. The supported transports are:
* SMTP
* Mailchimp / [Mandril](https://mandrill.com/)

#### SMS Notifications

The microservice supports sending SMS notifications. The supported transports are:
* [Twilio](https://www.twilio.com/) 

#### Push Notifications

The microservice supports sendong push notifications. The supported transposrts are:
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

Notifier is dockerized and can be easily deployed via docker. See the images on dockerhub:  
https://hub.docker.com/r/sflpro/notifier & https://hub.docker.com/r/sflpro/notifier-worker
