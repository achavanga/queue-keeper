# Queue Keeper Quarkus Project

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

While the code is surprisingly simple, under the hood this is using:
 - RESTEasy to expose the REST endpoints
 - Hibernate ORM with Panache to perform the CRUD operations on the database
 - A PostgreSQL database; see above to run one via Docker
 - ArC, the CDI inspired dependency injection tool with zero overhead
 - The high performance Agroal connection pool
 - Infinispan based caching
 - All safely coordinated by the Narayana Transaction Manager

## Requirements

To compile and run this project you will need:

- OpenJDK 11+
- Maven 3.6.2+

In addition, you will need either a PostgreSQL version 10.5 database, or Docker to run one.

### Configuring GraalVM and JDK 11

Make sure that both the `GRAALVM_HOME` and `JAVA_HOME` environment variables have
been set, and that a OpenJDK 11 `java` command is on the path.

See the [Building a Native Executable guide](https://quarkus.io/guides/building-native-image)
for help setting up your environment.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```
./mvnw quarkus:dev
```
## Running the Application

### Prepare a PostgreSQL instance

Make sure you have a PostgreSQL instance running. To set up a PostgreSQL database with Docker:

> docker run --ulimit memlock=-1:-1 -it --rm=true --memory-swappiness=0 --name queue_keeper -e POSTGRES_USER=abelc -e POSTGRES_PASSWORD=Corona2020 -e POSTGRES_DB=queue_keeper -p 5432:5432 postgres:10.5

Connection properties for the datasource are defined in the standard Quarkus configuration file,
`src/main/resources/application.properties`.

## Packaging and running the application

The application can be packaged using `./mvnw package`.
It produces the `queue-keeper-1.0.0-SNAPSHOT-runner.jar` file in the `/target` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/lib` directory.

The application is now runnable using `java -jar target/queue-keeper-1.0.0-SNAPSHOT-runner.jar`.

## Creating a native executable

You can create a native executable using: `./mvnw package -Pnative`.

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: `./mvnw package -Pnative -Dquarkus.native.container-build=true`.

You can then execute your native executable with: `./target/queue-keeper-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/building-native-image.

## API Documentation
Navigate to:

<http://localhost:8080/swagger-ui>

## Lesson Learned
* When creating entity classes using Panache make sure all variables are public and not private. If you mark them private your API will not bring back an value. You will only see the entity id(s) which is already public as it comes from PanacheEntity.
* No need to add Getters and Setter in entity classes.
* Advanced use cases may require a custom ID strategy, which can by done by extending PanacheEntityBase instead of PanacheEntity, and declaring a public id field with the necessary policy. For example:

```
@Id
@SequenceGenerator(name = "userSequence", sequenceName = "user_id_seq", allocationSize = 1, initialValue = 2)
@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userSequence")
public Long id;
```
* Had to change from @JsonIgnore to @JsonbTransient as I was getting a lot of fetch errors with error ""RESTEASY008205: JSON Binding serialization error Unable to serialize property ''".
   