#!/bin/bash
cd /${DEPLOY_PATH}
docker pull nadjim/bettor-league-api
docker-compose down
docker-compose up -d
