# Spring Boot Product Management Microservice

This project is a basic microservice built with Spring Boot to manage products, supporting CRUD operations. Data is stored in a PostgreSQL database, and API documentation is available via Swagger (SpringDoc OpenAPI).

## Prerequisites

To run this project, you need:

- Java 17
- Maven 3.x
- PostgreSQL (running database instance)

## Database Configuration

Create a PostgreSQL database and configure the connection in the application properties file. 
Add .env file with credentials for DB connection.

## Dependencies
Main dependencies:
- Spring Boot Starter Data JPA
- Spring Boot Starter Web
- Spring Boot Starter Validation
- PostgreSQL driver
- SpringDoc OpenAPI Starter
- Lombok

## API Documentation via Swagger (SpringDoc OpenAPI)
API documentation is available using Swagger UI. After running the project, go to the following URL:
"http://localhost:8080/swagger-ui.html"
    
## Features

- Spring Boot 3.x.x
- CRUD operations (Create, Read, Update, Delete) for products
- PostgreSQL for data storage
- JPA/Hibernate for ORM
- Validation of input data via Spring Validation
- API documentation using SpringDoc OpenAPI (Swagger)
- Global exception handling via `@ControllerAdvice`
- Lombok to reduce boilerplate code (getters/setters, etc.)
