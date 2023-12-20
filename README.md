# EMR
> Manage Inventory:.

## Important links

- [REST API docs](http://164.90.185.174:8084/swagger-ui/index.html#/)
- [api-docs](http://164.90.185.174:8084/api-docs)

## Getting started

Replace Application Properties values
Build Jar
```
bootJar
```
Docker Image creation and run:

```
docker build -t cards .
docker run -d --restart unless-stopped --name card_cnt -p 8084:8080 cards
```

Docker file

```
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY EMR-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
```



Insert Roles into the schema:
```
INSERT INTO roles(name) VALUES('ROLE_PHARMACIST');
INSERT INTO roles(name) VALUES('ROLE_ADMIN');
```
Continue with other API Operations as listed on the swagger Doc

## HOSTED SERVICE API Documentation

The API is documented using OpenAPI (Swagger).
```
SignUp,SignIn,List,Search,Create,CheckOut,SMS notification via Africastalking
```

BASE URL.
```
http://164.90.185.174:8084/
```

[REST API docs](http://164.90.185.174:8084/swagger-ui/index.html#/)

TESTS
```
Unit Tests : Ongoing
```

## CopyRight

Copyright 2023 Kogi. All Rights Reserved.
