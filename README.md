# 1. TrelloDemoKrasnopolskyi
The goal of this project is to develop a REST API for a Trello-like application,
allowing users to manage boards, columns and tasks.

# 2. Project description
The application contains columns with the ability to 
create, update, delete and change the order of columns on the same board.
Users can create, edit, delete, move tasks to different columns, 
and change the order of tasks within a column

# 3.  Start the project locally
## 3.1. Use to run project
•	Java 17
•	PostgreSQL 15.2 or higher
•	Docker

## 3.2. How to run
1. You should open in IntelliJ IDEA 
File -> New Project -> Project From Version Control -> Repository URL ->
URL (https://github.com/Tabernol/trelloDemoKrasnopolskyi) -> Clone.
2. Start Docker. (if you want to pass integration tests)
3. Run Application
4. When the program will have started, the liquibase will create a ‘krasnopolskyi’ scheme
where tables will be created and default data will be inserted.
5. If you did everything correctly, you should be able to access swagger by this URL:
http://localhost:8080/swagger-ui/index.html#/

# 4. Running Tests
To ensure the correctness of the application, integration tests are provided.
These tests require Docker to be running. Follow the steps below to run the tests:

## 4.1. Prerequisites
- Docker

## 4.2. Unit Tests
1. Open a terminal window and navigate to the project directory.
2. Run the following command to execute unit tests:
   ./gradlew test

## 4.3. Integration Tests
1. Start Docker if it's not already running.
2. Open a terminal window and navigate to the project directory.
3. Run the following command to execute integration tests:
   ./gradlew check