#!/bin/bash
docker pull nadjim/bettor-league-api
docker-compose -f /spring-boot-server/docker-compose.yml down
docker-compose -f /spring-boot-server/docker-compose.yml up -d
