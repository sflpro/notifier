#!/usr/bin/env bash

docker login -u "$DOCKER_USER" -p "$DOCKER_PASS"

init_central_auth() {
    tar -jxf secret.tar.bz2
    echo "Import gpg key"
    gpg --import secret/travis-gpg-key.asc
}

if [ "$TRAVIS_BRANCH" == "develop" ] && [ "$TRAVIS_PULL_REQUEST" == "false" ]
then
    init_central_auth
    echo "Running develop branch build and analysis. Snapshots will be published. All issues/stats will be saved to Sonar database."
    mvn -P snapshot -P central -s secret/settings.xml clean org.jacoco:jacoco-maven-plugin:prepare-agent deploy sonar:sonar -B \
    -Dsonar.host.url=https://sonarcloud.io \
    -Dsonar.organization=sfl \
    -Dsonar.login=$SONARCLOUD_KEY \
    -Dgpg.passphrase=$TRAVIS_GPG_KEY_PASS
elif [ $TRAVIS_BRANCH == 'master' ] && [ "$TRAVIS_PULL_REQUEST" == "false" ]
then
    init_central_auth
    echo "Running develop branch build and analysis. Snapshots will be published.. Sonar run will be skipped."
    mvn -P release -P central -s secret/settings.xml clean org.jacoco:jacoco-maven-plugin:prepare-agent deploy sonar:sonar -B \
    -Dsonar.host.url=https://sonarcloud.io \
    -Dsonar.organization=sfl \
    -Dsonar.login=$SONARCLOUD_KEY \
    -Dgpg.passphrase=$TRAVIS_GPG_KEY_PASS
elif [ "$TRAVIS_PULL_REQUEST" != "false" ]
then
    echo "Running Github PR build and analysis. Sonar will run in preview mode."
    mvn clean org.jacoco:jacoco-maven-plugin:prepare-agent verify sonar:sonar -B \
    -Dsonar.host.url=https://sonarcloud.io \
    -Dsonar.organization=sfl \
    -Dsonar.login=$SONARCLOUD_KEY \
    -Dsonar.analysis.mode=preview \
    -Dsonar.github.repository=sflpro/notifier \
    -Dsonar.github.pullRequest=$TRAVIS_PULL_REQUEST \
    -Dsonar.github.oauth=$SONAR_GITHUB_OAUTH_TOKEN
else
    echo "Running regular maven execution. No artifacts will be released to either release or snapshot repositories"
    mvn clean verify -B
fi
