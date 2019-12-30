# transaction-service ![Build Status](https://travis-ci.org/nishkarsh/transaction-service.svg?branch=master) [Only for Demonstration]
A sample TransactionService based on Jersey and embedded HttpServer that exposes APIs to transfer amount from one account to another.

### Building the Project
The project uses gradle as build tool and dependency management.

- Build: `./gradlew clean build`
- Tests [Unit & Integration]: `./gradlew test`
- Run Application: `./gradlew run`

A lightweight JDK HTTP Server serves the application on http://localhost:9000
To check if the service up, `GET http://localhost:9000/health-check` should return a `Status 200`

### Functionalities
The application exposes an endpoint to make an account transfer between accounts. There is no endpoint provided for accounts as of now.

To use pre-created accounts, use `778aab3a-1a5a-4fb9-bfb8-ff8a6b6c055f` and `96a5abd9-400b-4608-83ed-db3a126db40d` account IDs
 or add/alter these in `main/resources/META-INF/scripts/load-data.sql`

```
POST /transaction

{
	"remitterAccountId": "778aab3a-1a5a-4fb9-bfb8-ff8a6b6c055f",
	"beneficiaryAccountId": "96a5abd9-400b-4608-83ed-db3a126db40d",
	"amount": {
		"currencyCode": "EUR",
		"value": 20.0
	}
}
```

### Technologies Used
- Server and Framework:
  - Jersey as REST framework that provides a JAX-RS (JSR-370) implementation.
  - Jackson as ObjectMapper for Request/Response
  - Lightweight embedded JDK HttpServer
- Database:
  - H2 in-memory database with JPA and Hibernate
  - JBossStandAloneJtaPlatform for JTA - transaction management
- Language: Kotlin
- Testing: Junit 5 with JerseyTest, Mockito and RandomBeansExtension.
- Logging: Log4J

### Things kept in consideration
- Code modularity and readability
- Extensibility and Reusability
- Number of Junit Tests: 31. Good mix of Unit and Integration tests.
- Validations that are absolutely necessary
- Concurrency and Thread Safety. To test this a concurrency test has been written that performs 10 parallel account transfers across 5 accounts.
- Test Driven Development as a methodlogy has been followed to make sure that a scenario fails first and an implementation is really needed. Same goes for concurrency test as well.

### Known limitations & Future Scope
- Some basic validations have been kept out of scope. These include /transaction request validation on amount and accountIds as well.
- The currency conversion is not handled.
- Some of the exceptions are not mapped to proper response codes.
- The exception classes provided by different packages have been reused wherever the name of the exception makes sense instead of writing custom exception classes.
