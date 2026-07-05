# ToDoList API

A RESTful API for managing personal tasks, built with Spring Boot 3.5.x, Spring Security, and Spring Data JPA.

---

## Tech Stack

| Layer       | Technology                        |
|-------------|-----------------------------------|
| Language    | Java 21                           |
| Framework   | Spring Boot 3.5.x                 |
| Security    | Spring Security, JWT              |
| ORM         | Spring Data JPA (Hibernate)       |
| Database    | PostgreSQL                        |
| Mapping     | MapStruct                         |
| Testing     | JUnit 5, Mockito, JaCoCo          |
| Build Tool  | Maven                             |

---

## Features

**User Management**
- Register, login, and update user profile
- Password encryption with BCrypt
- Role-based access control (RBAC)

**Task Management**
- Full CRUD operations on personal tasks
- Task status lifecycle: `pending`, `in-progress`, `completed`
- Task assignment to users

**Authentication and Authorization**
- JWT-based stateless authentication
- Endpoint-level role restrictions via Spring Security

**Testing**
- Unit and integration tests with JUnit 5 and Mockito
- Code coverage reporting via JaCoCo

---

## Requirements

- Java 21
- Maven 3.8+
- PostgreSQL

---

## Getting Started

```bash
git clone https://github.com/psontay/todolist.git
cd todolist
```

Configure your PostgreSQL connection in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/todolist
spring.datasource.username=your_username
spring.datasource.password=your_password
```

Run the application:

```bash
./mvnw spring-boot:run
```

---

## API Endpoints

**Authentication**

| Method | Endpoint              | Description       |
|--------|-----------------------|-------------------|
| POST   | /api/auth/register    | Register new user |
| POST   | /api/auth/login       | Authenticate user |

**Users**

| Method | Endpoint           | Description          |
|--------|--------------------|----------------------|
| GET    | /api/users/me      | Get current user     |
| PUT    | /api/users/{id}    | Update user profile  |
| DELETE | /api/users/{id}    | Delete user account  |

**Tasks**

| Method | Endpoint           | Description       |
|--------|--------------------|-------------------|
| POST   | /api/tasks         | Create a task     |
| GET    | /api/tasks         | List all tasks    |
| PUT    | /api/tasks/{id}    | Update a task     |
| DELETE | /api/tasks/{id}    | Delete a task     |

---

## Running Tests

```bash
# Run all tests
./mvnw test

# Generate coverage report
./mvnw verify
open target/site/jacoco/index.html
```

---

## Project Structure

```
src/main/java/
├── controller       # REST controllers
├── service          # Business logic
├── repository       # Spring Data JPA repositories
├── entities         # JPA entities
├── dto              # Request and response DTOs
├── security         # JWT filter and security config
├── mapper           # MapStruct mappers
├── config           # Application configuration
└── exception        # Global exception handling
```

---

## Contributing

Pull requests are welcome. For significant changes, please open an issue first to discuss the proposed modification.
