# sept-frontend
## Usage
### nodejs
1. Run `npm install` in the sept-react-frontend directory to download and install dependencies
2. Before starting the react front, ensure that the guide for the backend below has been followed
3. Run `npm start` to start the react application
### Login details
admin
username: dr_evil
password: test_password
worker
username: no2
password: another_test_password
customer
username: John_Smith
password: test_password

# sept-backend
## Usage
### Maven
1. Run `mvn install` in the sept-backend directory to download and install dependencies
2. Either using your IDE or CLI, run the application using `java -jar target/sept-backend-<version>.jar`
### Docker
> NOTE:  When running the application using docker, the server will attempt to connect to the deployed DB instance, rather than starting an in-memory database. To run an in-memory database using docker, remove "--spring.profiles.active=deploy" from the ENTRYPOINT command

1. Ensure you have docker (version >=18.09) and docker-compose (version >=1.25.1) installed (this is due to using the
experimental BuildKit features)
2. Run `COMPOSE_DOCKER_CLI_BUILD=1 DOCKER_BUILDKIT=1 docker-compose up --build sept-backend` to build and start running the application (will take some time to download the dependencies
on the first run)
