# 📝 ToDoList API

> A robust and secure RESTful API for managing personal tasks, built with **Spring Boot**, **Spring Security**, and **Spring Data JPA**.

---

## 🚀 Features

- ✅ **User Management**
  - Register, login, update profile
  - Password encryption with BCrypt
  - Role-based access control (RBAC)

- 📋 **Task Management**
  - CRUD operations on personal tasks
  - Update status (pending, in-progress, completed)
  - Assign tasks to users

- 🔐 **Authentication & Authorization**
  - JWT-based authentication
  - Secure API endpoints with role restrictions

- 🧪 **Testing**
  - Unit & Integration tests with **JUnit 5**, **Mockito**
  - Code coverage tracking via **JaCoCo**

- 📊 **Database**
  - PostgreSQL integration with Spring Data JPA
  - Automatic schema generation with Hibernate

---

## 🛠️ Tech Stack

| Layer          | Technology                  |
|----------------|-----------------------------|
| Language       | Java 21                     |
| Framework      | Spring Boot 3.5.x           |
| Security       | Spring Security, JWT        |
| ORM            | Spring Data JPA (Hibernate) |
| Database       | PostgreSQL                  |
| Mapping        | MapStruct                   |
| Testing        | JUnit 5, Mockito, JaCoCo     |
| Build Tool     | Maven                       |

---

## ⚙️ Getting Started

### 📦 Requirements

- Java 21
- Maven 3.8+
- PostgreSQL

### 🧑‍💻 Run Locally

```bash
# Clone the repository
git clone https://github.com/sontay226/todolist.git
cd todolist

# Configure PostgreSQL DB in src/main/resources/application.properties

# Build & run
./mvnw spring-boot:run
📌 Example Endpoints
🔐 Authentication
http
Copy
Edit
POST /api/auth/login
POST /api/auth/register
👤 User
http
Copy
Edit
GET /api/users/me
PUT /api/users/{id}
DELETE /api/users/{id}
✅ Task
http
Copy
Edit
POST /api/tasks
GET /api/tasks
PUT /api/tasks/{id}
DELETE /api/tasks/{id}
🧪 Run Tests
bash
Copy
Edit
# Unit & integration tests
./mvnw test

# View coverage (after configured)
./mvnw verify
open target/site/jacoco/index.html
🧠 Folder Structure
graphql
Copy
Edit
├── controller       # REST Controllers
├── service          # Business logic
├── repository       # Spring Data JPA repositories
├── entities         # JPA Entities
├── dto              # Request/Response DTOs
├── security         # JWT and config
├── mapper           # MapStruct mappers
├── config           # Application config
└── exception        # Custom exceptions
📮 Contact
Made with ❤️ by @sontay226
Feel free to contribute or report issues.
