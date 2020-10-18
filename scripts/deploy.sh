cd majorproject-8-mon-17-30-1
git checkout master
git pull
cd BackEnd/sept-backend
docker-compose down
COMPOSE_DOCKER_CLI_BUILD=1 DOCKER_BUILDKIT=1 docker-compose up --build sept-backend
