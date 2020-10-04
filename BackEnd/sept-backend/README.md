# sept-backend
## Usage
### Maven
1. Run `mvn install` in this directory to download and install dependencies
2. Either using your IDE or CLI, run the application using `java -jar target/sept-backend-<version>.jar` 
### Docker
1. Ensure you have docker (version >=18.09) and docker-compose (version >=1.25.1) installed (this is due to using the
experimental BuildKit features)
2. Run `docker-compose up` to build and start running the application (will take some time to download the dependencies
on the first run)