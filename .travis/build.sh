#!/usr/bin/env bash

docker login -u "$DOCKER_USER" -p "$DOCKER_PASS"

if [ "$TRAVIS_BRANCH" == "develop" ] && [ "$TRAVIS_PULL_REQUEST" == "false" ]
then
    echo "Running develop branch build and analysis. Snapshots will be published. All issues/stats will be saved to Sonar database."
    mvn -P snapshot -P central clean org.jacoco:jacoco-maven-plugin:prepare-agent deploy sonar:sonar -B \
    -Dsonar.host.url=https://sonarcloud.io \
    -Dsonar.organization=sfl \
    -Dsonar.login=$SONARCLOUD_KEY
elif [ $TRAVIS_BRANCH == 'master' ] && [ "$TRAVIS_PULL_REQUEST" == "false" ]
then
    mvn -P release -P central clean org.jacoco:jacoco-maven-plugin:prepare-agent deploy sonar:sonar -B \
    -Dsonar.host.url=https://sonarcloud.io \
    -Dsonar.organization=sfl \
    -Dsonar.login=$SONARCLOUD_KEY
elif [ "$TRAVIS_PULL_REQUEST" != "false" ]
then
    echo "Running Github PR build and analysis. Sonar will run in preview mode."
    mvn clean org.jacoco:jacoco-maven-plugin:prepare-agent verify sonar:sonar -B \
    -Dsonar.host.url=https://sonarcloud.io \
    -Dsonar.organization=sfl \
    -Dsonar.login=$SONARCLOUD_KEY \
    -Dsonar.analysis.mode=preview \
    -Dsonar.github.repository=sflpro/identity \
    -Dsonar.github.pullRequest=$TRAVIS_PULL_REQUEST \
    -Dsonar.github.oauth=$SONAR_GITHUB_OAUTH_TOKEN
else
    mvn clean verify -B
fi