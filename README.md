### Running the project

1 - Clone the repository and switch to directory

```bash
git clone https://github.com/fpsoriano/campsite.git
```

2- Build the project:

```bash
mvn clean install
```


3 - Execute the next command to run the application and all dependencies:
```
docker-compose up
```

OBS: If you have some problem with the option above, follow the next steps to run the application


4- You can run next two command to run the application as well
```
docker-compose up
```

```
mvn spring-boot:run
```

```
http://localhost:8080/swagger-ui.html#/
```

-----------------------------------

### Accessing to the swagger documentation you will be able to:
1- Access each endpoint end test using the available examples.

2- Check the allowed values for each field
```
http://localhost:8080/swagger-ui.html#/
```

### Postman COllection
To make easy, I create a postman collection with all endpoints. So it is just to import and call the APIs
