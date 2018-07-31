#!/bin/bash
docker pull nadjim/bettor-league-api
docker-compose -f /$DEPLOY_PATH/docker-compose.yml down
docker-compose -f /$DEPLOY_PATH/docker-compose.yml up -d
