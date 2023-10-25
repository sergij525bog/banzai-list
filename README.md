# banzai-list

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:
```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using: 
```shell script
./mvnw package -Pnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/banzai-list-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.

## Related Guides

- REST resources for Hibernate ORM with Panache ([guide](https://quarkus.io/guides/rest-data-panache)): Generate JAX-RS resources for your Hibernate Panache entities and repositories
- RESTEasy Classic ([guide](https://quarkus.io/guides/resteasy)): REST endpoint framework implementing JAX-RS and more
- Hibernate ORM with Panache ([guide](https://quarkus.io/guides/hibernate-orm-panache)): Simplify your persistence code for Hibernate ORM via the active record or the repository pattern


## Entities and comments

- Item - element of product list. Has only one type and category.
- List - named list that contains various number of items.
- Type - task or product
- Category - categories of tasks (home, work, sales etc) and products (vegetables, fruits, fish etc)
- Difference between tasks and products - it is need to create two different classes
- Item - interface for Task and Product?
- Place - entity related to task or product. Have a category, address and time of availability
- Notification - for user

## Features

- List, Item, Category - CRUD
- Category, priority - add icons
- Tasks or products should be automatically linked to Place by their Category
- Tasks may be cycled
- Notifications 
-- by address - if user belong near Place (required geolocation)
-- by date - if task or product has end date (required end date)
-- if list contains some task for a long time
- List items may have priority
- Sorting in alphabet order, by date, by priority, category
- Show date info if needed
- Should categories of items include specific fields?
- Set category by product name (required map of categorie -> product list)

