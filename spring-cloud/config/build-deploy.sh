#!/usr/bin/env bash

rm -r ./target
mkdir target

spring jar --classpath application.yml target/configserver.jar configserver.groovy

cf push

cf cups config-server -p '{"uri":"http://todo-config-server.cfapps.io"}'