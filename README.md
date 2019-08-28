[![Build Status](https://prd-tcp-jenkins.excellalabs.com/buildStatus/icon?job=TCP%2FJava%2Ftcp-java%2Fmaster)](https://prd-tcp-jenkins.excellalabs.com/job/TCP/job/Java/job/tcp-java/job/master/)
[![Coverage](http://sonarqube.excellalabs.com:9000/api/project_badges/measure?project=tcp-java&metric=coverage)](http://sonarqube.excellalabs.com:9000/dashboard?id=tcp-java)
[![Vulnerabilities](http://sonarqube.excellalabs.com:9000/api/project_badges/measure?project=tcp-java&metric=vulnerabilities)](http://sonarqube.excellalabs.com:9000/dashboard?id=tcp-java)


# tcp-java repository

A CRUD example using Spring WebFlux and Java 11


##### 1. Install:
  - [docker](https://docs.docker.com/engine/installation/)
  - [docker-compose](https://docs.docker.com/compose/install/)
  - keytool: this is part of the JDK; if you don't already have it: `sudo apt-get install openjdk-11-jdk`
      -For mac run: `brew cask install java`

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

##### 9. Deployment to the ECS Cluster

To deploy to the ECS cluster, you need to
build the production Docker image, push it to the ECR,
use ecs-cli to create and bring up the service, and open ports in the load balancer.

1. Installing and Configuring the aws and ecs-cli Command-Line Tools

    Follow the instructions from the tcp-ecs repo's README, in the
    section titled "Installing and Configuring the aws and ecs-cli Command-Line Tools".
    https://github.com/excellaco/tcp-ecs/

1. Build the production Docker image:

    From the root directory:
    `docker build -t tcp-java-api:latest .`

    Note: you may need to run this as root: `sudo docker build -t tcp-java-api:latest .`
    This step will take about 2 minutes.  It uses the `Dockerfile`.

    Do `docker image ls` to make sure the image was built: `tcp-java-api`

1. Push the image to the Elastic Container Repository (ECR):

    `ecs-cli push --aws-profile default tcp-java-api:latest`

    or, if necessary,

    `sudo ecs-cli push --aws-profile default tcp-java-api:latest`

1. Use ecs-cli to create a new task and bring up the service:

    Go to the ecs subdirectory

    Note: all `ecs-cli compose` commands for tcp-java *must* be run from this subdirectory.

    `ecs-cli compose --aws-profile default service ps`

    Make sure the service is not running (either it doesn't show up, or it
    shows up with State = STOPPED). If it is running, do:

    `ecs-cli compose --aws-profile default service down`

    and wait for completion.

    The following command will both create the task and bring up the service on the ECS cluster:

    `ecs-cli compose --aws-profile default service up`

    This will take about 20 seconds.

    Double-check that the service is running:

    `ecs-cli compose --aws-profile default service ps`

    You should see both `/api` and `/db` containers.

    This will also tell you which host(s) it's running on.

1. Scale up the service (Optional)

    `ecs-cli compose --aws-profile default service scale 2`

1. Enable connections from the internet to the tcp-java containers

    [In the future, we will replace this step with Terraform automation.]

    - a) Create a new target group; make its name end with "-to-8080-tg".
        Make sure to select the correct VPC.
        Set its type to Instance.  Set its protocol to HTTP. Set its target port to 8080.
        Do *not* register any instances with it directly: the Auto-Scaling Group (ASG) will do that for us.

    - b) Go to the cluster node ASG (name contains "-cluster-node") and attach the new target group:
        "Details" tab, "Edit" button.  Click in the "Target Groups" box, type "8080", select the new target group.
        Click "Save".

    - c) Remove any exsiting Listeners on the ALB that are listening on port 8080.
        Add a Listener to the ALB: listen on 8080, forward to the new target group.

    - d) Make sure the ALB's Security Group (name ends with "-alb-sg") allows connections on port 8080.
         Also note the ALB's ARN for the next step.

    - e) Set the cluster node security group (name ends with "-cluster-instance-sg") to
        accept connections on 8080 from the ALB's Security Group.

1. Check that the API service is running and accessible

    You can get the ALB's FQDN by running: `terraform output cluster_url`

    From a machine that can connect to the ALB (e.g. a machine on Excella's network):

    From the project root directory,

    `alb=[fqdn of ALB here, no port]`
    `./get-token $alb:8000`

    This will ask the API at `:8080` on the ALB to create a bearer token, then store that token in
    a file called `token`.  The token file can be used with CURL to access the rest of the API

    Run:
    `curl -K token $alb:8080/api/employee/2`

    You should get back a JSON response with information about the employeed with ID=2.

1. Troubleshooting

    If either of the above commands fails, try `ssh`ing into one of the cluster nodes, going to
    the tcp-java root directory, and running:
    `./get-token localhost:8080`
    `curl -K token localhost:8080/api/employee/2`
    If this succeeds, the problem is probably with the networking (ALB, Target Groups, Security Groups).
