# ToDoList API

A RESTful API for managing personal tasks, built with Spring Boot 3.5.x, Spring Security, and Spring Data JPA.

---

## Tech Stack

- Java 21
- Spring Boot 3.5.x
- Spring Security + OAuth2 Resource Server (JWT)
- Spring Data JPA + Hibernate
- Spring Data Redis
- Spring Boot Mail + Thymeleaf
- PostgreSQL
- MapStruct
- Lombok
- SpringDoc OpenAPI (Swagger UI)
- JUnit 5, Mockito, JaCoCo
- Maven + Spotless (Google Java Format)

---

## Features

- User registration, login, and profile management
- Password encryption with BCrypt
- Role-based access control
- Full CRUD on personal tasks
- Task status lifecycle: `pending` → `in-progress` → `completed`
- JWT-based stateless authentication via OAuth2 Resource Server
- Email notifications with Thymeleaf-rendered templates
- Redis caching layer
- API documentation via Swagger UI at `/swagger-ui.html`
- Unit and integration tests with coverage reporting via JaCoCo
- Code formatting enforced via Spotless (Google Java Format)

---

## Requirements

- Java 21
- Maven 3.8+
- PostgreSQL
- Redis

---

## Getting Started

```bash
git clone https://github.com/psontay/todolist.git
cd todolist
```

Configure your environment in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/todolist
spring.datasource.username=your_username
spring.datasource.password=your_password

spring.data.redis.host=localhost
spring.data.redis.port=6379

spring.mail.host=smtp.gmail.com
spring.mail.username=your_email
spring.mail.password=your_app_password
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

Full interactive documentation available at `/swagger-ui.html` when running locally.

---

## Testing

```bash
# Unit tests
./mvnw test

# Integration tests + coverage report
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
