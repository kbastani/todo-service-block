#!/bin/bash

curl http://localhost:3333/v1/todos -X POST -d '{ "title": "Note example" }' -H "Content-Type: application/json"