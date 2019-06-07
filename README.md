# spring-reactor-template

A CRUD example using Spring WebFlux and Java 11


##### 1. Install [docker](https://docs.docker.com/engine/installation/) and [docker-compose](https://docs.docker.com/compose/install/)

##### 2. Running the application

- Compile the app: `./start clean build`

- Run the application: `./start bootrun`

- Navigate to:
    > http://localhost:8080/api/swagger-ui.html

- Running tests: `./start test`

- Run the linter: `./start verGJF`

- Auto-fix linter warnings: `./start goJF`


##### 3. Setting up IntelliJ

- Install the Lombok Plugin

    1. Go to `File > Settings > Plugins`.
    2. Click on Browse repositories...
    3. Search for Lombok Plugin.
    4. Click on Install plugin.
    5. Restart IntelliJ IDEA.
    
- Import the project
    1. Select `Import Project`
    2. Select the directory where you cloned the repository
    3. Import as a gradle project and select a JDK >= 11
    4. Select to use the gradle wrapper configuration from the project

