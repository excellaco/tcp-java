[![Build Status](https://prd-tcp-jenkins.excellalabs.com/buildStatus/icon?job=TCP%2FJava%2Ftcp-java%2Fmaster)](https://prd-tcp-jenkins.excellalabs.com/job/TCP/job/Java/job/tcp-java/job/master/)
[![Coverage](http://sonarqube.excellalabs.com:9000/api/project_badges/measure?project=tcp-java&metric=coverage)](http://sonarqube.excellalabs.com:9000/dashboard?id=tcp-java)
[![Vulnerabilities](http://sonarqube.excellalabs.com:9000/api/project_badges/measure?project=tcp-java&metric=vulnerabilities)](http://sonarqube.excellalabs.com:9000/dashboard?id=tcp-java)


# tcp-java repository

A CRUD example using Spring WebFlux and Java 11

## Prerequisites

For local development, you will need:

1. [Git](www.git-scm.com)
    * **Windows Users** [You need to preserve Unix line endings on checkout](https://help.github.com/en/articles/configuring-git-to-handle-line-endings) because shells don't like carriage return. Run `git config --global core.autocrlf input`. Ideally you did this before cloning; if not, re-checkout after changing this setting. If you don't do this, git auto-converts LF to CRLF (\r) and the shell scripts will break if run locally.
1. [Docker Compose](https://docs.docker.com/compose/install/) (docker-compose)
    * Linux: It might be easiest just to use your package manager to install the `docker-compose` package. The official instructions can overly complicate it, and also leave off all but the most popular distros.
    * **Windows Users**: make sure you have Win 10 *Professional*, or you may not be able to install the latest Docker tools due to lack of Hyper-V support.
1. <u>JDK >= 11</u> for IDE tooling. To check for a suitable JDK, use `java -version`, making sure it is not a JRE. If you don't have one:
    * Linux: Install the latest OpenJDK package using your distro's package manager. Add the installed binary folder to your PATH and set JAVA_HOME, replacing references to any older JDKs, if the package didn't do it for you.
    * Mac (Homebrew): `brew cask install java`
    * Windows:  Install the [latest JDK](https://www.oracle.com/technetwork/java/javase/downloads/index.html). Edit the system environment variables to add the binary folder to PATH and set JAVA_HOME, unless the installer does this for you.
        

Docker (Engine) version 18.09.8 is known to work;
docker-compose version 1.24.1 is known to work; docker-compose version 1.17.1 is known **not** to work.
Older versions may not support the docker-compose 3.7 file format, which this project uses.

You can check the installed versions with:
```bash
docker --version
docker-compose --version
```

## Quickstart
1. Install the [prerequisites](#prerequisites) above.
1. Clone this repository: `git clone git@github.com:excellaco/tcp-java.git`
1. Open a terminal in the root of the cloned project. <u>Windows users should open a Git Bash</u>, which comes with Git for Windows and GitHub Desktop.
1. Run `docker-compose run api bash`. Use this bash prompt for the next step.
1. Run `./generate-keystore`. It doesn't really matter what information you enter here.
1. Run `exit` once to drop out of the docker container.
1. Run `./start bootrun` and wait for the backend to start up
1. Go to http://localhost:8080/api/swagger-ui.html to see interactive API documentation.
1. Before you perform requests from Swagger, click the small lock icon and enter `user` / `pass`. 

* Terminal or application complaining about \r characters? See the Windows warning under the Git prerequisite.

## Detailed Start and Development Setup

Windows users - as in the Quickstart, you should use Git Bash.

### Getting the code

There are two primary ways to pull the code for a standalone backend - a clone of this repository, and the XG tool.
In both cases, if you're on Windows *make sure you followed the [instructions](#prerequisites) about line endings above*. The XG tool may clone the repository under the hood, so the warning still applies.

#### Clone this repository

`git clone git@github.com:excellaco/tcp-java.git` 

#### Using the "XG" Command-Line Interface (CLI)
  - Download and setup the [XG Tool, also known as TCP CLI](https://github.com/excellaco/xg#installation):
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

### Set up the application
This project uses OAuth2 and JWT for authentication / authorization.

In order for the app to work correctly, you must create a Java KeyStore (.jks) file in the classpath (i.e., in src/main/resources).  We have created a script for this purpose.

From the project root, run the following command(s):

1. `docker-compose run api bash` to get a session on a docker container with the project mounted.
2. `./generate-keystore`
3. `exit` once to drop out of the docker session.

The generate-keystore script is interactive. It will ask you for a keystore filename (e.g. mytest.jks) and a keystore password.
It will then create an .env file for you, then run `keytool` behind the scenes. This will prompt for several user inputs. You can enter dummy data, as this information will only be used in your local environment.

### Run the application

1. Compile the app: `./start clean build`
    * Note: if a script gives permission denied, you may need to run `chmod a+x script_name`
    * Note: if the build fails on the format check, run this and then try again: `./start goJF`. If this is a fresh checkout, go yell at whoever merged the format violations to master.
    * Note: if you get a lot of FileNotFoundException test failures, double check that you ran the `generate-keystore` script. There should be a .env file in the app's root directory and a \*.jks file in `src/main/resources`
1. Run the application: `./start bootrun`
1. Navigate to: 
	> http://localhost:8080/api/swagger-ui.html
1. Click the lock button to pop up a login prompt. The default credentials are `user` / `pass` .

### Common Development Tasks

When the app is not running:

* Run the tests: `./start testNG`
* Run the linter: `./start verGJF`
* Auto-fix linter warnings (do this on the last commit before pushing!): `./start goJF`
* Run linter + tests + test report: `./test` (coverage report is generated under `build/jacocoHtml/index.html`)

### Setting up IntelliJ

- Install the Lombok Plugin

    1. Go to `File > Settings > Plugins`.
    2. Click on Browse repositories...
    3. Search for Lombok Plugin.
    4. Click on Install plugin.
    5. Restart IntelliJ IDEA.
    6. Ensure Annotation Processing is enabled `File > Settings > Build, Execution, Deployment > Compiler > Annotation Processors`. (IDEA may prompt you to do this)

- Import the project
    1. Select `Import Project`
    2. Select the directory where you cloned the repository
    3. Import as a gradle project and select a JDK >= 11
    4. Select to use the gradle wrapper configuration from the project

### Use OAuth to interact with the API (Cleanup TODO)

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


### Tech Challenge Architecture

The Java repository represents one of the possible backends that can be used.

![Tech Challenge Architecture Diagram](architechture.png)

To connect a front-end application to the API, please see either the Angular or React tech challenge repositories.

  - [Angular Repository](https://github.com/excellaco/tcp-angular)
  - [React Repository](https://github.com/excellaco/tcp-react)

### Deployment to the ECS Cluster (Deprecated by Terraform / scripts? - TODO)

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

    Go to the tcp-java-ecs subdirectory

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
    
### Suggested Reading

#### Reactive Web Frameworks

[WebFlux](https://spring.io/guides/gs/reactive-rest-service/) is a reactive web framework for Spring. For more information on reactive design and its basic principles, we suggest looking at the [Reactive Manifesto](https://www.reactivemanifesto.org/).

For a more detailed guide to understanding how to handle reactive and functional types like `Mono` and `Flux`, please refer to our [Java Reactive Tutorial](https://github.com/excellalabs/reactive-in-java)
