[![Build Status](https://sandbox-tcp-jenkins.excellalabs.com/buildStatus/icon?job=TCP%2FJava%2Ftcp-java%2Fmaster)](https://sandbox-tcp-jenkins.excellalabs.com/job/TCP/job/Java/job/tcp-java/job/master/)

# tcp-java repository

A CRUD example using Spring WebFlux and Java 11


##### 1. Install:
  - [docker](https://docs.docker.com/engine/installation/)
  - [docker-compose](https://docs.docker.com/compose/install/)
  - keytool: this is part of the JDK; if you don't already have it: `sudo apt-get install openjdk-11-jdk`

Docker (Engine) version 18.09.8 is known to work;
docker-compose version 1.24.1 is known to work; docker-compose version 1.17.1 is known to NOT work.
Older versions may not support the docker-compose 3.7 file format, which this project uses.
You can check the installed versions with:
```
docker --version
docker-compose --version
```


##### 2. Using the Command-Line Interface (CLI)
  - Download and setup the [TCP CLI](https://github.com/excellaco/xg#installation):
    this consists of a single binary which can be put in your PATH.
  - Copy the `xg.yaml` file from this repository to where you will run the CLI (you do not need to clone the entire repository)
  - Fill out the appropriate fields in `xg.yaml` with the desired values: (can quotes be used?)
    - projectName (directory)
    - ProjectName (for Gradle)
    - GroupName (for Gradle; typically "com.excella")
  - run `xg make`
    - Note: Your `xg` must be 0.2.1 (7/23/19) or later; `xg version` will show the installed version.
    - Note: if your private key for the tcp-java GitHub repo is not in `~/.ssh/id_rsa`, specify it with the '-i' or '--identity' flag:
      `xg make -i ~/.ssh/my_github_key` .  Failure to provide the correct private key will result in the error message `Error: Unable to clone repository remote`.
    - This will list a bunch of files and directories, prefixed with "F" for a file, "T" for a template, and "D" for a directory.
    - The result is in a subdirectory whose name is the same as the projectName you specified in `xg.yaml`.  Here we assume it's called my-project.
    - The result is *not* a Git workspace: there's no `.git` subdirectory.
  - Work around minor current `xg` limitations:
    - `cp -p xg.yaml my-project` # xg puts the version of xg.yaml as found in the repo under my-project

##### 3. Setup the application
This project uses OAuth2 and JWT for authentication / authorization.

[TODO: document the new generate-keystore script]

In order for the app to work correctly, you must create a Java KeyStore (.jks) file in the classpath (i.e., in src/main/resources).  There is an automated way and a manual way to do this.

##### A) Automated JKS setup

From the project root, run the following command:

`./generate-keystore`

It will ask you for a keystore filename (e.g. mytest.jks) and a keystore password.
It will create an .env file, then run `keytool`, which will ask you a bunch of questions.

##### B) Manual JKS setup

Create the JKS using the following command, from the project root:
```
keytool -genkeypair -alias server -keyalg RSA -keypass mypass -keystore src/main/resources/mytest.jks -storepass mypass
```

Note: the two passwords (keypass and storepass) must be the same.

`keytool` will ask you a bunch of questions.

Next, add an .env file in the project root and populate it with the same info you specified in the `keytool` command above.
These values will be automatically used in the application.yml file.

Here's what your .env file should look like.

```$xslt
JWT_KEY_STORE=classpath:mytest.jks
JWT_KEY_STORE_PASS=mypass
JWT_KEYPAIR_ALIAS=server
JWT_KEYPAIR_PASS=mypass
```

Once the app is running, you'll be able to get a token from it and call the rest of its API. (See below.)

##### C) Flyway setup

Also, please ensure Flyway migrations are running. They include the DB setup for OAuth2 and Users. See the following for more information

 - https://docs.spring.io/spring-security/site/docs/5.0.x/reference/html5/#appendix-schema
 - https://github.com/spring-projects/spring-security-oauth/blob/master/spring-security-oauth2/src/test/resources/schema.sql


##### 4. Run the application

- Compile the app: `./start clean build`
  - Note: you forgot to do `chmod a+x start test gradlew` above, didn't you? Do it now.
  - Note: if you don't want the fancy font coloring and status bars, you can do `./start --console plain clean build`
  - Note: if the build fails on the format check, run this and then try again: `./start goJF`
  - Note: if you get a lot of FileNotFoundException test failures, check that your keystore (`.jks`) file is in the CLASSPATH and is readable

- Run the application: `./start bootrun`

- Navigate to:
    > http://localhost:8080/api/swagger-ui.html

When the app is not running:

- Run the tests: `./start testNG`

- Run the linter: `./start verGJF`

- Auto-fix linter warnings: `./start goJF`

- Run linter + tests + test report: `./test` (coverage report is generated under `build/jacocoHtml/index.html`)

##### 5  Use OAuth to interact with the API

There are two ways you can use OAuth to interact with the app's API from the command line:

##### A) Automated OAuth token request

From the project root directory, 

`./get-token`

This will ask the API at `localhost:8080` to create a bearer token, then store that token in a file called `token`.  The token file can be used with CURL to access the rest of the API, for example:
`curl localhost:8080/api/employee/2 -K token`

##### B) Manual OAuth token request

While the app is running (`./start bootrun`):

**TEMPORARY DEFAULT CREDENTIALS**:
```
client-id: app
client-secret: $2a$04$hqawBldLsWkFJ5CVsvtL7ed1z9yeoknfuszPOEHWzxfLBoViK6OVi
username: user
password: pass
```

You must retrieve a token before hitting any other endpoints. To do so, create a POST request to `localhost:8080/api/oauth/token`. Use Basic Auth (with client id and secret) in the header with the username, password, and grant_type in the payload:

`curl --basic -u 'app:$2a$04$hqawBldLsWkFJ5CVsvtL7ed1z9yeoknfuszPOEHWzxfLBoViK6OVi' -d username=user -d password=pass -d grant_type=client_credentials localhost:8080/api/oauth/token`

You can use the token returned in the JSON response to the above POST in other API requests:

`curl localhost:8080/api/employee/2 -H "Authorization: Bearer BIGLONGTOKENFROMTHEJSON"`

You can make this easier by creating a file called (e.g.) `token`, and putting the -H stuff in it:
```
-H  "Authorization: Bearer BIGLONGTOKENFROMTHEJSON"
```

Then you can call curl more concisely:
`curl localhost:8080/api/employee/2 -K token`


##### 6. Set up IntelliJ

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



##### 7. Tech Challenge Architecture

The Java repository represents one of the possible backends that can be used.

![Tech Challenge Architecture Diagram](architechture.png)

To connect a front-end application to the API, please see either the Angular or React tech challenge repositories.

  -  [Angular Repository](https://github.com/excellaco/tcp-angular)
  - [React Repository](https://github.com/excellaco/tcp-react)

##### 8.  Reactive Web Frameworks

[Springboot Webflux](https://spring.io/guides/gs/reactive-rest-service/) is a reactive web framework.  For more information on reactive design and its basic principles, we suggest looking at the [Reactive Manifesto](https://www.reactivemanifesto.org/).

For a more detailed guide to understanding how to handle reactive and functional types like `Mono` and `Flux`, please refer to our [Java React Tutorial](https://github.com/excellalabs/reactive-in-java)

