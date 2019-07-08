# CI Status
[![Build Status](https://travis-ci.org/sflpro/notifier.svg?branch=master)](https://travis-ci.org/sflpro/notifier)

# Notifications Microservice
The notifications microservice is a separataely deployable component intended for sending various types of notifications. It provides a common abstraction layer on top of the various notification types and actual sending, tracking routines.
Currently microservice supports :
* Email notifications
* SMS notifications
* Push notifications

### Email Notifications

The microservice supports sending regular emails. The supported transports are :
* SMTP
* Mandrill

### SMS Notifications

The microservice supports sending SMS notifications. The supported transports are :
* [Twilio](https://www.twilio.com/) 
* [MsgAm](https://www.msg.am/) 

### Push Notifications

The microservice supports sendong push notifications. The supported transposrts are :
* [Amazon SNS](https://aws.amazon.com/sns/)
* [Firebase Cloud Messaging FCM](https://firebase.google.com/docs/cloud-messaging)

## Microservice API

The public API of the microservice is exposed via HTTP REST. API client libraries are available in the followign languages :
* Java

However, any platform supporting HTTP calls can use the microservice by manually implementing HTTP calls execution logic.

## Deployment

Currently the microservice is packaged as a WAR file, hence requiring a servlet container which can be used for running it. Tested containers are :
* [Apache Tomcat 8] (http://tomcat.apache.org/)
* [Jetty](https://eclipse.org/jetty/)

## TODOs

Below you may find some of the actions planned for the near future :
* Convert the application to use [Spring Boot/Cloud](http://projects.spring.io/spring-cloud/)
* Document the REST API in [Apiary](https://apiary.io/)
* Add client libraries in Python
* Add support for other SMS providers
* Expose the API via [GRPC](http://www.grpc.io/)
