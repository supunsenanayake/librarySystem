
# Library System 
## Technology Stack
 1. Java 17: The latest long-term support version with enhancements in performance and usability.
 2. Spring Boot: Simplifies the development of new Spring applications through convention over configuration.
 3. Maven: Handles project dependencies and builds.
 4. H2 Database: An in-memory database that is easy to set up and suitable for development and testing phases. For production, you might consider a more robust option like PostgreSQL(since I didn't have time to set up PostgreSQL db I have used Mysql ) for its reliability and rich feature set.
 5. JUnit and Mockito for Testing: Standard libraries for unit and integration testing in Java projects.

## Project Structure
The project would have the following main components:
* Borrower: Entity and repository to manage borrower data.
* Book: Entity and repository to manage book data including handling     multiple copies with the same ISBN.
* LibraryService: Business logic for borrowing and returning books.
* LibraryController: RESTful endpoints for interacting with the library system.

## Data Models
### Borrower
* id: Unique identifier (auto-generated)
* name: Name of the borrower
* email: Email address of the borrower

### Book
* id: Unique identifier (auto-generated)
* isbn: ISBN number of the book
* title: Title of the book
* author: Author of the book
* borrowedBy: Borrower id
## API Endpoints
URL FOR swagger-ui: http://localhost:2222/swagger-ui/index.html#/
* POST /api/borrowers - Register a new borrower.

```
curl --location 'localhost:2222/api/borrowers' \
--header 'Content-Type: application/json' \
--data-raw '{
    "id":"borrowers1",
    "name":"supun",
    "email":"supunsnnk2@sdsa.com"
}'
```
* POST /api/books - Register a new book.

```
curl --location 'localhost:2222/api/books' \
--header 'Content-Type: application/json' \
--data '{
    "id":"bookid1",
    "isbn":"isbn1",
    "title":"AAAAAAAAAAAAAAAAAAAAA",
    "author":"SADADADADAD"
}'
```
* GET /api/books - Retrieve a list of all books.

```
curl --location 'localhost:2222/api/books' \
--data ''
```
* PUT /api/borrowers/{borrowerId}/books/{bookId}/borrow - Borrow a book by id.

```
curl --location --request PUT 'localhost:2222/api/borrowers/borrowers1/books/bookid1/borrow' \
--data ''
```
* PUT /api/borrowers/{borrowerId}/books/{bookId}/return - Return a borrowed book.

```
curl --location --request PUT 'localhost:2222/api/borrowers/borrowers1/books/bookid1/return' \
--data ''
```

## Key Features and Considerations
* Concurrent Borrowing: Synchronization at the application or database level to ensure a book can only be borrowed by one person at a time.
* Validation: Using Spring's @Valid annotation to enforce input validations.
* Error Handling: Global error handler using @ControllerAdvice to manage exceptions and return proper HTTP status codes.

## Database Choice
H2 is chosen for development due to its simplicity and ease of configuration. For production, a transition to PostgreSQL would be recommended for its robustness, support for concurrent transactions, and extensive industry adoption.
## Configuration for Multiple Environments
Use Spring profiles to configure different settings for development, testing, and production environments. Configuration files like application-dev.properties, and application-prod.properties would help manage these environments.

## Documentation
* Swagger or Springdoc OpenAPI for interactive API documentation.
* README.md to include setup instructions, environment configuration details, and basic usage.

## Assumptions
* Borrower's email is unique and can be used as a secondary identifier.
* Each book borrowal is tracked as a unique event, allowing for historical borrowing data without affecting the current state of book availability.
* The system does not manage overdue books or fines; it only tracks the state of a book (borrowed/available).

## Future Enhancements (Nice to Have)
* Dockerizing the application for easier deployment.
* Using Jenkins or GitHub Actions for CI/CD.
* Writing comprehensive unit and integration tests with Mockito and JUnit.
* Adhering to the 12 Factor App methodology, especially regarding config, logging, and disposability.

## How to run on CMD line Interface
* Configure DB username and password in application.properties (Location: application-dev.properties or application-prod.properties)
* Go to project folder librarySystem Open the CMD interface in the project folder and run "mvn clean install"
* After building successfully, run the command. “mvn spring-boot:run”

