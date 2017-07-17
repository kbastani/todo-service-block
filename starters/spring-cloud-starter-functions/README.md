# Spring Cloud Starter Functions

This starter provides auto-configuration support classes to invoke _Spring Cloud Function_ services from other Spring Boot applications. A `FunctionRegistry` is used to lookup and invoke functional services on a variety of different serverless platforms.

Serverless platforms maintain their own service registries that are used to lookup and invoke a function by its name. One of the major inconveniences that this causes is that it becomes difficult to develop and run your functions locally. This project provides a `DiscoveryClient` adapter that allows you to invoke functions that are registered with a discovery service, such as Eureka or Consul.

The following adapters are currently supported:

- AWS Lambda
- Discovery Client

## AWS Lambda

This project provides an auto-configured adapter for looking up and invoking functions hosted on AWS Lambda from a Spring Boot application.

The following support is provided.

* Configuration property classes for externalizing AWS credentials as IAM access keys.
* Authenticated session management for securely invoking Lambda functions.
* A Spring Boot friendly `FunctionInvoker` bean that allows you to easily swap between different platforms without changing code.

### Usage

In your Spring Boot application, add the starter function dependency to your classpath. For Maven, add the following dependency to your `pom.xml`.

```xml
<dependencies>
    <dependency>
        <groupId>org.kbastani</groupId>
        <artifactId>spring-cloud-starter-functions</artifactId>
        <version>${spring-cloud-starter-functions.version}</version>
    </dependency>
    
    ...
</dependencies>
```

Next, configure your AWS credentials in your application properties.

```yaml
spring:
  profiles: cloud
  cloud:
    function:
      adapter: aws_lambda
amazon:
  aws:
    access-key-id: ${AWS_ACCESS_KEY_ID}
    access-key-secret: ${AWS_ACCESS_KEY_SECRET}
```

You can also set the properties using command line arguments with the Maven Spring Boot plugin.

```bash
$ mvn spring-boot:run -Drun.arguments="--amazon.aws.access-key-id=ABCDEFG,--amazon.aws.access-key-secret=ZYXKGFWG"
```

You can now begin to invoke AWS Lambda functions from your AWS todo. The next thing you'll need to do is to define an interface of lambda function references to invoke.

```java
public interface AccountFunctions extends FunctionRegistry {
    
    @LambdaFunction(functionName="account-created", logType = LogType.Tail)
    Account accountCreated(AccountEvent event);
}
```

A `FunctionInvoker` bean will be auto-configured and then used to retrieve proxied instances of a `FunctionRegistry` interface.

```java
@Service
public class AccountService {
	
    private final AccountFunctions accountFunctions;
	
    public AccountService(FunctionInvoker functionInvoker) {
        this.accountFunctions = functionInvoker.getRegistry(AccountFunctions.class);
    }
    
    public Account createAccount(Account account) {
        return accountFunctions.accountCreated(new AccountEvent(account));
    }
}
```

## Discovery Client

A `DiscoveryClient` adapter is provided to allow you to invoke functions that are registered with a discovery service, such as Eureka or Consul, without changing any business logic. This adapter is primarily used in local development environments where invoking functions on a remote platform requires a deployment.

### Usage

Make sure your project is a discovery service client using `@EnableDiscoveryClient`. Configure your application properties, as shown below.

```yaml
spring:
  profiles: development
  cloud:
    function:
      adapter: discovery_client
 ```

Enable your Spring Cloud Function applications to also use service discovery with `@EnableDiscoveryClient`. Make sure that the name of the function in your `FunctionRegistry` interfaces match the name used to register with a discovery service.

Function registry  in your core Spring Boot application:

    public interface AccountFunctions extends FunctionRegistry {
        
        @LambdaFunction(functionName="account-created", logType = LogType.Tail)
        Account accountCreated(AccountEvent event);
    }

Application properties in your Spring Cloud Function application:

```yaml
spring.application.name: account-created
 ```
 
 Your application will now invoke the `account-created` function using a `DiscoveryClient` instead of AWS Lambda.