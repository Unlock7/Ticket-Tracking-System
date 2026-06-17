<h1 align="center">📌 Ticket Tracking System</h1>
<h2 align="center">Mid Backend-Track Project</h2>
<h3 align="center">(HackYourFuture)</h3>


A backend application built as part of the **HackYourFuture Mid Backend Track**, implementing a complete ticket tracking system with:

* User management
* Project management
* Ticket creation & updates
* Ticket assignment
* Email notifications
* Validation & error handling
* Secure environment configuration
* Controller & service layer testing
* The project follows clean architecture principles and uses **Spring Boot 4**, **Java 25**, **PostgreSQL**, and **dotenv** for secure configuration.

# **🧱 Architecture Overview**

Code

```
┌────────────┐       ┌──────────────┐       ┌──────────────┐       ┌──────────────┐
│ Controller │  -->  │ Request DTO  │  -->  │   Service    │  -->  │  Repository  │  --> DB
└────────────┘       └──────────────┘       └──────────────┘       └──────────────┘
       ↑                      ↓
       └────────────── Response DTO ────────────────┘

```

* **Controller Layer** — REST endpoints
* **Service Layer** — business logic, validation, email notifications
* **Repository Layer** — JDBC queries using `JdbcTemplate`
* **Database Layer** — PostgreSQL schema

# **🗄️ Database Schema**

The system uses four tables:

* `users`
* `projects`
* `tickets`
* `ticket_assignees` (many‑to‑many)

Relationships:

* One **project** → many **tickets**
* Many **users** ↔ many **tickets**

# **📂 Project Structure**

Based on your real folder structure:

Code

```
src/main/java/net/hackyourfuture/tickettrackingsystem
│
├── controller
│   ├── ProjectController
│   ├── TicketController
│   └── UserController
│
├── dto
│   ├── project
│   │   ├── ProjectRequest
│   │   └── ProjectResponse
│   ├── ticket
│   │   ├── TicketRequest
│   │   └── TicketResponse
│   └── user
│
├── exception
│   ├── ErrorResponse
│   ├── GlobalExceptionHandler
│   ├── NotFoundException
│   └── ValidationException
│
├── model
│   ├── Project
│   ├── Ticket
│   ├── TicketAssignee
│   └── User
│
├── repository
│   ├── ProjectRepository
│   ├── TicketAssigneeRepository
│   ├── TicketRepository
│   └── UserRepository
│
├── service
│   ├── EmailNotificationService
│   ├── ProjectService
│   ├── TicketService
│   ├── UserService
│   └── TicketTrackingSystemApplication
│
└── resources
    ├── db/TrackTicket_schema.sql
    └── application.properties
```

# 

# **🧪 Test Structure**

You mentioned:

> **Tests are only made for controller and service**

Your test folder:

Code

```
src/test/java/net/hackyourfuture/tickettrackingsystem
│
├── controller
│   ├── ProjectControllerTest
│   ├── TicketControllerTest
│   └── UserControllerTest
│
├── service
│   ├── ProjectServiceTest
│   ├── TicketServiceTest
│   └── UserServiceTest
```

✔ Uses **MockMvc** for controller tests ✔ Uses **Mockito** for service tests ✔ Matches HYF expectations for mid‑backend level



# **🔧 Secure Dotenv Loading**

Inside `TicketTrackingSystemApplication`:

java

```
Dotenv dotenv = Dotenv.load();

System.setProperty("DB_URL", dotenv.get("DB_URL"));
System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
System.setProperty("EMAIL_API_KEY", dotenv.get("EMAIL_API_KEY"));
System.setProperty("EMAIL_FROM", dotenv.get("EMAIL_FROM"));
```

✔ No secrets printed ✔ No secrets committed ✔ Works in CI/CD



# **⚙️ application.properties**

Code

```
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=org.postgresql.Driver
```



# **🚀 Running the Application**

### **1. Install dependencies**

Code

```
mvn clean install
```

### **2. Start PostgreSQL**

Code

```
docker run --name postgres -e POSTGRES_PASSWORD=pass -p 5432:5432 -d postgres
```

### **3. Run the app**

Code

```
mvn spring-boot:run
```

# **📡 API Summary**

Your API is fully documented in `API_DESIGN.md`. Here is the summary:

### **Users**

* GET /users
* GET /users/{id}
* POST /users
* PUT /users/{id}
* DELETE /users/{id}

### **Projects**

* GET /projects

### **Tickets**

* POST /tickets
* GET /tickets
* GET /tickets/{id}
* PUT /tickets/{id}
* POST /tickets/{ticketId}/assignees
* DELETE /tickets/{ticketId}/assignees/{userId}

# **📨 Email Notifications**

Emails are sent when:

* A ticket is **updated**
* A user is **assigned**

Emails include:

* Ticket ID
* Title
* Description
* Status

If sending fails:
* Ticket update still succeeds
* Error is logged

# 🔄 CI/CD Pipeline
GitHub Actions pipeline includes:
Java 25
Maven build
Tests
Cache optimization

Triggers:

push → main  
pull_request → main
# 🤝 Contribution Guidelines
* Write clean, readable code
* Follow naming conventions
* Keep controllers thin
* Put logic in services
* Validate all inputs
* Add tests for new features
* Use meaningful commit messages
