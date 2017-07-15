# Serverless Spring Cloud Function Example

This example application is based on the [AngularJS TodoMVC sample](http://todomvc.com/). The backend is implemented using Spring Boot and Spring Cloud.

There are two microservices in this example:

- Todo Core
- Todo UI

### Todo Core

The `todo-core` microservice is the system of record for managing TODO items.

This microservice has the following Spring Boot starter projects:

- Spring Boot Starter JPA
- Spring Boot Starter Web
- Spring Boot Starter Actuator
- Spring Boot Starter HATEOAS
- Spring Boot Starter Test

And the following Spring Cloud starters:

- Spring Cloud Starter Eureka
- Spring Cloud Starter Hystrix
- Spring Cloud Starter Config

### Todo UI

The `todo-ui` microservice is the stateless front-end containing the AngularJS static content for managing TODO items.

This microservice has the following Spring Boot starter projects:

- Spring Boot Starter Web
- Spring Boot Starter Actuator
- Spring Boot Starter Test

And the following Spring Cloud starters:

- Spring Cloud Starter Eureka
- Spring Cloud Starter Hystrix
- Spring Cloud Starter Config

### Development Environment

This example has been designed to allow you to easily bootstrap your development environment in a single command. The two microservices need to be able to find each other in the environment they are running in. To enable this, we'll use tooling in the Spring Cloud project. 

Spring Cloud provides two services that the microservices will depend on:

- Spring Cloud Eureka
- Spring Cloud Config Server

To run these services locally, you'll need to install the [Spring Cloud CLI](https://cloud.spring.io/spring-cloud-cli/). The Spring Cloud CLI will allow you to launch Eureka and the Config Server in a single command.

    $ spring cloud configserver eureka

To make this easier, I've created a bootstrap shell script that will orchestrate the startup of each application in the microservice architecture.

    $ sh run.sh

This script will run each of the 4 JVM applications in this example in a single terminal. **This script will kill any other running Java processes**. Make sure that you comment out the `killall java` line before executing the script, if you're running any Java processes that should not be shut down. If you're using Windows, make sure to run this command using bash.

#### Eureka

After starting up the services in your development environment, navigate to `http://localhost:8761` to see the Eureka dashboard. Here you will be able to see the microservices that have registered for discovery.

#### Config Server

The config server will run on `http://localhost:8888`. To modify the location of the git repository that the config server will use to centralize configuration, modify the git uri in `.config/configserver.yml` of this repository.

    spring:
      profiles:
        active: default
      application:
        name: config-server
    ---
    spring:
      profiles: default
      cloud:
        config:
          server:
            git:
              # change uri
              uri: https://github.com/kbastani/todo-service-config
              basedir: target/config
    server:
      port: 8888
    management:
      security:
        enabled: false

#### Todo UI

The Todo UI will be available at `http://localhost:8080`. It's recommended that you use developer tools in your browser to see whether or not the UI is able to communicate with the `todo-core` microservice. You can launch developer tools in most browsers using the `ALT+CMD+I` hot key. I don't know what this would be for Windows, so do some Googling if you must!

### Production Environment

The production environment for this application is managed by Cloud Foundry. You can use any up-to-date distribution of Cloud Foundry that you'd like. For this example, we will try to use [Pivotal Web Services](http://run.pivotal.io). Because PWS requires that you provide billing information when signing up, you can alternatively use [PCF Dev](https://pivotal.io/platform/pcf-tutorials/getting-started-with-pivotal-cloud-foundry-dev/introduction) to run Cloud Foundry on your development machine.

#### Concourse

This example has a pre-configured deployment pipeline that will build and deploy the microservices in this example project to Cloud Foundry. The deployment manifest for creating the pipeline is located in `deployment/deploy.yml`.

##### Credentials

The deployment manifest allows you to provide a configuration file that contains your Cloud Foundry API credentials. You must update this file before deploying to a CF environment. The file is located in `deployment/.pipeline-config`.

    pws-api: https://api.run.pivotal.io
    pws-username: replace
    pws-password: replace
    pws-org: replace
    pws-space: replace

##### Spring Cloud Services

The Concourse pipeline in this example will not deploy a discovery service or config server. You will have to do this yourself before starting the pipeline. The deployment scripts for deploying Eureka and the Config Server are located in `./spring-cloud`.

For each service, run the following convenience scripts.

**Config Server**:

    $ cd ./spring-cloud/config
    $ sh build-deploy.sh

**Eureka**:

    $ cd ./spring-cloud/discovery
    $ sh build-deploy.sh

You must have the Cloud Foundry CLI installed to deploy these two services. Each service will be created using a random route, and the route will be bound as a service credential to the TODO microservices as a part of the pipeline.

##### Create Pipeline

To create the pipeline you'll need to install the Concourse CLI, called `fly`. To install `fly`, navigate to the Concourse server we'll be using for this example, located here: http://ci.qcon.oskoss.com/

Follow the directions for downloading and installing `fly`. After the installation is complete, go ahead and create your deployment pipeline using the following command.

    fly -t lite set-pipeline -p [unique-name] -c deployment/deploy.yml -l deployment/.pipeline-config.yml

**Make sure** to replace `[unique-name]` with an identifier you will both remember and no one else will be using in the example.

Fly will ask you to login with your CI server. To do that, you'll need to use the following credentials:

  - Username: admin
  - Password: admin

Once you've created your pipeline, go to the Concourse UI and start it. Make sure you find the unique name you used when you created the pipeline. Concourse will then build and deploy the two microservices in this project.
