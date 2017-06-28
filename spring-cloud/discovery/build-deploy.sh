#!/usr/bin/env bash

rm -r ./target
mkdir target

spring jar --classpath application.yml target/eureka.jar eureka.groovy

cf push

cf cups discovery-service -p '{"uri":"http://todo-discovery-service.cfapps.io"}'