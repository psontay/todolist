# ToDoList API

A RESTful API for managing personal tasks, built with Spring Boot 3.5.x, Spring Security, and Spring Data JPA.

---

## Tech Stack

- Java 21
- Spring Boot 3.5.x
- Spring Security + JWT
- Spring Data JPA + Hibernate
- PostgreSQL
- MapStruct
- JUnit 5, Mockito, JaCoCo
- Maven

---

## Features

- User registration, login, and profile management
- Password encryption with BCrypt
- Role-based access control
- Full CRUD on personal tasks
- Task status lifecycle: `pending` → `in-progress` → `completed`
- JWT-based stateless authentication
- Unit and integration tests with coverage reporting via JaCoCo

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

Configure your database in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/todolist
spring.datasource.username=your_username
spring.datasource.password=your_password
```

```bash
./mvnw spring-boot:run
```

---

## API Endpoints

**Auth**
- `POST /api/auth/register`
- `POST /api/auth/login`

**Users**
- `GET    /api/users/me`
- `PUT    /api/users/{id}`
- `DELETE /api/users/{id}`

**Tasks**
- `POST   /api/tasks`
- `GET    /api/tasks`
- `PUT    /api/tasks/{id}`
- `DELETE /api/tasks/{id}`

---

## Testing

```bash
./mvnw test
./mvnw verify
open target/site/jacoco/index.html
```

---

## Project Structure

```
src/main/java/
├── controller
├── service
├── repository
├── entities
├── dto
├── security
├── mapper
├── config
└── exception
```

---

## Contributing

Pull requests are welcome. For significant changes, open an issue first to discuss the proposed modification.
