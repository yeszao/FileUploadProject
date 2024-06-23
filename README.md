# FileUploadProject
This is a project that allows users to upload files to a server and download them. The project is built using `Spring Boot` and `MongoDB`.

## Features
- Upload files to the server
- Download files from the server
- View all uploaded files with filtering and sorting options
- Delete uploaded files
- Rename uploaded filenames

## Technologies
- Java 17
- Spring Boot
- MongoDB
- Hibernate
- JPA
- JUnit

## Setup
1. Clone the repository.
2. Open the project in your IDE.
3. Setup a MongoDB database at `application.yml`.
4. Run the project.

## Usage
1. Open your browser and go to [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html).
2. Here you can see all the available endpoints in Swagger page and test them.

## How to build JAR file
```shell
./gradlew clean build
```
After building, the JAR file will be located at `build/libs`.