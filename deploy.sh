#!/bin/bash
git pull
docker pull nadjim/bettor-league-api
docker-compose down
docker-compose up -d
