#!/usr/bin/env bash

docker login -u "$DOCKER_USER" -p "$DOCKER_PASS"

if [ $TRAVIS_BRANCH == 'develop' ]
then
    mvn -P snapshot clean org.jacoco:jacoco-maven-plugin:prepare-agent deploy sonar:sonar -B \
    -Dsonar.host.url=https://sonarcloud.io \
    -Dsonar.organization=sfl \
    -Dsonar.login=$SONARCLOUD_KEY
elif [ $TRAVIS_BRANCH == 'master' ]
then
    mvn -P release clean org.jacoco:jacoco-maven-plugin:prepare-agent deploy sonar:sonar -B \
    -Dsonar.host.url=https://sonarcloud.io \
    -Dsonar.organization=sfl \
    -Dsonar.login=$SONARCLOUD_KEY
else
    mvn clean verify -B
fi