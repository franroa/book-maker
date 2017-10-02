#!/bin/bash
set -ex

docker kill $(docker ps -q) > /dev/null 2>&1 || true
docker rm -v $(docker ps -a -q -f status=exited) > /dev/null 2>&1 || true

docker run --name dictionary_db_test -p 54322:5432 -e POSTGRES_PASSWORD=password -e POSTGRES_USER=postgres -e POSTGRES_DB=dictionary_db_test -d postgres:alpine