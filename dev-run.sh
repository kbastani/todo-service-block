#!/usr/bin/env bash

RED='\033[0;31m'
BLUE='\033[0;34m'
NC='\033[0m'

export CURRENT_PATH=$PWD
export SPRING_PROFILES_ACTIVE="development"

function log {
    while IFS= read -r line; do echo "[$1] $line"; done >&1
}

function random_color {
    echo "\033[0;3$(( $RANDOM * 6 / 32767 + 1 ))m$1${NC}"
}

function app {
    APP_NAME=$(random_color $3)
    ./mvnw -f ./$1 spring-boot:run | log $APP_NAME
}

function wait_for_service {
    while [ -z ${SERVICE_READY} ]; do
      echo "[$3] Waiting for $1..."
      if [ "$(curl --silent $2 2>&1 | grep -q '\"status\":\"UP\"'; echo $?)" = 0 ]; then
          SERVICE_READY=true;
      fi
      sleep 2
    done
}

function apps {
    LOG_LABEL=$(random_color "APPLICATIONS")

    # Apps need to wait for the configuration server to start up
    wait_for_service "configuration server" "http://localhost:8888/health" $LOG_LABEL

    ( app "todo-core" "$LOG_LABEL" "TODO-CORE" ; kill -TERM -$$ >/dev/null 2>&1 ) &
    app "todo-ui" "$LOG_LABEL" "TODO-UI" ; kill -TERM -$$ >/dev/null 2>&1

    wait
}

function services {
    cd ./cloud-services
    sh run.sh
}

function functions {
    LOG_LABEL=$(random_color "FUNCTIONS")
    cd $CURRENT_PATH
    app "todo-functions/metrics-function" $LOG_LABEL "METRICS-FUNCTION" ; kill -TERM -$$ >/dev/null 2>&1
    wait
}

# Build the service blocks
./mvnw clean install -DskipTests | log $(random_color "BUILD")

# Launch services -> apps -> functions
( services; kill -TERM -$$ >/dev/null 2>&1 ) &
( apps; kill -TERM -$$ >/dev/null 2>&1 ) &
functions