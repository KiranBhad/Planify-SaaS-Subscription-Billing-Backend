# Planify - SaaS Subscription Billing Backend

Planify is a backend system built using **Spring Boot** that powers a SaaS Subscription Billing Platform. It handles user authentication, role-based access (ADMIN/USER), subscription plans, email verification, and more.

---

## 🔧 Tech Stack

- **Java 17**
- **Spring Boot**
- **Spring Security**
- **JWT (JSON Web Token)**
- **MySQL / PostgreSQL**
- **Spring Data JPA**
- **Maven**
- **Lombok**
- **Mail API (JavaMailSender)**

---

## 🚀 Features

- 🔐 **JWT-based Authentication**
- 🧑‍💻 Role-based Access Control (`USER` & `ADMIN`)
- 📬 Email Verification
- 📦 Subscription Plan Management (Create, Update, Delete)
- 🧾 User Plan Subscription with Expiry Tracking
- 💌 Email Notification System (Subscription Confirmations, Expiry Reminders, etc.)
- 📡 RESTful API

---


---

## 🔑 API Endpoints (Sample)

### Auth
- `POST /api/auth/register` – Register a new user
- `POST /api/auth/login` – Login with email and password

### User
- `GET /api/user/me` – Get current user profile
- `POST /api/user/subscribe` – Subscribe to a plan

### Admin
- `POST /api/admin/plans` – Create a new plan
- `GET /api/admin/users` – List all users

---




✅ TODOs
 JWT Auth

 Email Verification

 Admin: Plan CRUD

 User Subscription

 Payment Integration (Coming Soon)

 Dashboard & Analytics

 Reset Password Feature



 🧑‍💻 Author
 Kiran Bhad
📫 Email: bhadkiran1804@gmail.com
