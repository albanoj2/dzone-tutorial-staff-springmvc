# DZone Spring MVC Staff Tutorial
This repository contains the code examples that accompany the DZone staff presentation on Spring MVC given by Justin Albano on May 15, 2020.

## Running the Tests
Before running the tests, the following must be installed:

# Java
# Maven

To run the unit tests for this example, execute the following on the command line:

```
mvn clean test
```

## Running the Application
To start the application, execute the following on the command line:

```
mvn spring-boot:run
```

This will start the application on `http://localhost:8080`.

## Exercising the Live Application
With the application started, we can create a new article using the following command:

```
curl -X POST http://localhost:8080/article \
  --data '{"title": "My Article", "author": "Justin Albano"}' \
  -H "Content-Type: application/json"
```

This command results in:

```json
{
    "id": "548240dc-4240-4483-acf0-583cf14e85be",
    "title": "My Article",
    "author": "Justin Albano"
}
```

When can then view this article by executing the following command:

```
curl -X GET http://localhost:8080/article
```

This command results in:

```json
[
    {
        "id": "548240dc-4240-4483-acf0-583cf14e85be",
        "title": "My Article",
        "author": "Justin Albano"
    }
]
```
