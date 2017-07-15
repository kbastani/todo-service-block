#!/usr/bin/env bash

NC='\033[0m'

export CURRENT_PATH=$PWD

function log {
    while IFS= read -r line; do echo "[$1] $line"; done >&1
}

function random_color {
    echo "\033[0;3$(( $RANDOM * 6 / 32767 + 1 ))m$1${NC}"
}

function clean {
    cd $CURRENT_PATH
    rm -rf ./$1/target
}

function run_service {
    clean $1
    cd $CURRENT_PATH/$1
    mkdir target
    spring jar ./target/$1.jar ./$1.groovy
    java -jar ./target/$1.jar | log $(random_color $2)
}

trap 'clean configserver && clean discovery' TERM

( run_service discovery "EUREKA-SERVER"; kill -TERM -$$ >/dev/null 2>&1 ) &
run_service configserver "CONFIG-SERVER"; kill -TERM -$$ >/dev/null 2>&1