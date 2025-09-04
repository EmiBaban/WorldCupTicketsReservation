# World Cup Tickets Reservation 🎟️  

## 📌 Overview  
This project is a **backend application** built with **Spring Boot**, designed to manage ticket reservations for the **2026 FIFA World Cup**.  
It provides robust CRUD operations for core entities such as **Stadium**, **Match**, and **Reservation**, and features secure authentication and authorization using **JWT tokens** and role-based access control.

---

## 🚀 Features  
- **Authentication & Authorization**
  - JWT-based authentication
  - Role-based permissions (`admin`, `user`, `manager`)
  - Controller-level authorization
- **Full CRUD operations** for:
  - Stadiums
  - Matches
  - Ticket Reservations
  - Users
- **DTOs** for request/response payloads
- **Email confirmation** on reservation via **MailTrap**
- **Consistent error handling** with standard HTTP codes
- **ORM & Repository Pattern** including entity relationships:
  - One-to-One
  - One-to-Many
  - Many-to-Many
- **Containerized PostgreSQL** using `docker-compose`

---

## 🛠️ Tech Stack  
- **Java 17**
- **Spring Boot 3.x** (Spring Data JPA, Spring Security)
- **PostgreSQL** (via Docker Compose)
- **Maven** for build management
- **MailTrap** for email testing

---

## ⚙️ Setup & Run  

### 1️⃣ Clone the repository  
```bash
git clone https://github.com/EmiBaban/WorldCupTicketsReservation.git
cd WorldCupTicketsReservation
```

### 2️⃣ Start the PostgreSQL database with Docker Compose  
```bash
docker-compose up -d
```
The database runs on `localhost:5432` with default credentials:
- user: `postgres`
- password: `postgres`
- database: `postgres`

You can use [PGAdmin](https://www.pgadmin.org/) or [DBeaver](https://dbeaver.io/download/) to connect and inspect the database.

### 3️⃣ Build & run the backend application  
```bash
mvn clean install
mvn spring-boot:run
```

---

## 🔑 Authentication & Roles

Authentication is handled via JWT tokens:

- **ROLE_ADMIN**: Can manage stadiums, matches, manage users and assign role as manager to users
- **ROLE_USER**: Can make ticket reservations
- **ROLE_MANAGER**: Can manage stadiums, matches

Include your JWT token in the request headers:
```
Authorization: Bearer <your_token>
```

---

## 📡 API Endpoints (Examples)

### Auth
- `POST /auth/register` — Register a new user
- `POST /auth/login` — Login, returns JWT token

### Stadiums
- `GET /stadiums` — List all stadiums
- `POST /stadiums` — Add a stadium (manager only)
- `PUT /stadiums/{id}` — Update stadium (manager only)
- `DELETE /stadiums/{id}` — Delete stadium (manager only)

### Matches
- `GET /matches` — List all matches
- `POST /matches` — Add a match (manager only)

### Users
- `GET /getAllUsers` — List all users (admin only)
- `DELETE /deleteUser/{id}` — Delete user (admin only)
- `PUT /assignRole/{id}` — Assign manager role to user (admin only)

---

## 📧 Email Notifications

Every ticket reservation triggers a confirmation email sent to the user via **MailTrap** (for safe email testing in development).

---

> Built for educational purposes and learning modern backend development with Spring Boot.
