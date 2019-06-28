# spring-reactor-template

A CRUD example using Spring WebFlux and Java 11


##### 1. Install [docker](https://docs.docker.com/engine/installation/) and [docker-compose](https://docs.docker.com/compose/install/)


##### 2. Setup the application
This project uses OAuth2 and JWT for authentication / authorization.

In order for this to work correctly, you must create a .jks file in the classpath using the following command:
```
keytool -genkeypair -alias server 
                    -keyalg RSA 
                    -keypass mypass 
                    -keystore mytest.jks 
                    -storepass mypass
```
Note: it's recommended you use the values in the application.yml file. 

Next, add an .env file and populate it with the same info you specified in the `keytool` command above.

Here's what your .env file should look like.

```$xslt
JWT_KEY_STORE=classpath:mytest.jks
JWT_KEY_STORE_PASS=mypass
JWT_KEYPAIR_ALIAS=server
JWT_KEYPAIR_PASS=mypass
```

**TEMPORARY DEFAULT CREDENTIALS**: 
```
client-id: app
client-secret: $2a$04$hqawBldLsWkFJ5CVsvtL7ed1z9yeoknfuszPOEHWzxfLBoViK6OVi
username: user
password: pass
```

You must retrieve a token before hitting any other endpoints. To do so, create a POST request to `/oauth/token`. Use Basic Auth (with client id and secret) in the header with the username, password, and grant_type in the payload.

Also, please ensure Flyway migrations are running. They include the DB setup for OAuth2 and Users. See the following for more information
 
 - https://docs.spring.io/spring-security/site/docs/5.0.x/reference/html5/#appendix-schema
 - https://github.com/spring-projects/spring-security-oauth/blob/master/spring-security-oauth2/src/test/resources/schema.sql


##### 3. Run the application

- Compile the app: `./start clean build`

- Run the application: `./start bootrun`

- Navigate to:
    > http://localhost:8080/api/swagger-ui.html

- Running tests: `./start testNG`

- Run the linter: `./start verGJF`

- Auto-fix linter warnings: `./start goJF`

- Run linter + tests + test report: `./test` (coverage report is generated under `build/jacocoHtml/index.html`)


##### 4. Set up IntelliJ

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
    


##### 5. Tech Challenge Architecture

The Java repository represents one of the possible backends that can be used.

![Tech Challenge Architecture Diagram](architechture.png)

To connect a front-end application to the API, please see either the Angular or React tech challenge repositories.

  -  [Angular Repository](https://github.com/excellaco/tcp-angular)
  - [React Repository](https://github.com/excellaco/tcp-react)

To use the Command-Line Interface (CLI), please download and setup the [CLI Repository](https://github.com/excellaco/xg)

##### 6.  Reactive Web Frameworks

[Springboot Webflux](https://spring.io/guides/gs/reactive-rest-service/) is a reactive web framework.  For more information on reactive design and its basic principles, we suggest looking at the [Reactive Manifesto](https://www.reactivemanifesto.org/).

For a more detailed guide to understanding how to handle reactive and functional types like `Mono` and `Flux`, please refer to our [Java React Tutorial](https://github.com/excellalabs/reactive-in-java)

