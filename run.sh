#!/usr/bin/env bash

RED='\033[0;31m'
BLUE='\033[0;34m'
NC='\033[0m'

function log {
    while IFS= read -r line; do echo "[$1] $line"; done >&1
}

function apps {
    while [ -z ${CONFIG_SERVICE_READY} ]; do
      echo "Waiting for config service..."
      if [ "$(curl --silent http://localhost:8888/admin/health 2>&1 | grep -q '\"status\":\"UP\"'; echo $?)" = 0 ]; then
          CONFIG_SERVICE_READY=true;
      fi
      sleep 2
    done

    while [ -z ${DISCOVERY_SERVICE_READY} ]; do
      echo "Waiting for discovery service..."
      if [ "$(curl --silent http://localhost:8761/health 2>&1 | grep -q '\"status\":\"UP\"'; echo $?)" = 0 ]; then
          DISCOVERY_SERVICE_READY=true;
      fi
      sleep 2
    done

    ./mvnw -f ./todo-block/todo-core spring-boot:run -Drun.profiles=development &
    ./mvnw -f ./todo-ui spring-boot:run -Drun.profiles=development
    wait
}

./mvnw clean install -DskipTests | log "${BLUE}BUILD${NC}"
wait
spring cloud configserver eureka | log "${BLUE}SERVICES${NC}" &
apps | log "${RED}APPLICATIONS${NC}"
wait

