###  Overview
APIs to manage a parking reservation. You can create, update, cancel and retrieve available reservations dates/details

###  pre-requirements

* JDK 11
* Maven
* Docker
* Docker-compose

(if you don't have docker you can install mongodb and run the application via terminal)

```bash
mvn clean install
```
```
mvn spring-boot:run
```

### Running the project

1 - Clone the repository and switch to directory

```bash
git clone https://github.com/fpsoriano/campsite.git
```

2- Build the project:

```bash
mvn clean install
```

3- Build the image:

```bash
docker build --tag campsite-image -f Dockerfile .
```

4 - Execute the next command to run the application and all dependencies:
```
docker-compose up
```

OBS: If you have some problem with the option above, follow the next steps to run the application


4- You can run next two command to run the application as well

```bash
docker build --tag campsite-image -f Dockerfile .
```

```
docker-compose up
```

```
mvn spring-boot:run
```

```
http://localhost:8080/swagger-ui.html#/ (incognito browser)
```

-----------------------------------

### Accessing to the swagger documentation you will be able to:
1- Access each endpoint end test using the available examples.

2- Check the allowed values for each field
```
http://localhost:8080/swagger-ui.html#/ (incognito browser)
```

### Postman COllection
To make easy, I create a postman collection with all endpoints. So it is just to import and call the APIs
