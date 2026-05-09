# TruelyFit
A Gym Management & AI Coaching Platform

A production-grade REST API for comprehensive gym management, built with Spring Boot. Manage members, trainers, workout plans, diet plans, and attendance tracking with role-based access control and JWT authentication.

**Live Demo:** Running on `http://localhost:8080` | **API Docs:** Swagger UI at `/swagger-ui.html`

---

## 🎯 Features

### ✅ Authentication & Security
- **JWT Token-based Authentication** — stateless, scalable
- **Role-Based Access Control** — ADMIN, TRAINER, MEMBER roles
- **BCrypt Password Encryption** — industry standard
- **Method-level Authorization** with `@PreAuthorize`

### ✅ Member Management
- Member registration and profiles
- Goal tracking (Weight Loss, Muscle Gain, Maintenance, Flexibility)
- Trainer assignment per member
- Member status (Active, Inactive, Suspended)

### ✅ Trainer Management
- Trainer profiles with specialization and bio
- Experience tracking
- Experience years and certifications

### ✅ Attendance Tracking
- Daily attendance marking (Present/Absent)
- Attendance history per member
- Date-range filtering
- Attendance percentage calculation
- Monthly/weekly summaries

### ✅ Workout Planning
- Trainer creates customized workout plans for members
- Multiple exercises per plan with ordering
- Exercise details: sets, reps, weight, duration
- Plan status management (Active, Inactive, Completed)
- Exercise notes and modifications

### ✅ Diet Planning
- Trainer assigns nutrition plans to members
- Meal-based organization (Breakfast, Lunch, Dinner, Snacks)
- Macro tracking: Calories, Protein, Carbs, Fats
- Daily nutritional summary calculation
- Food quantity and notes

### ✅ Error Handling
- **Global Exception Handler** — consistent error responses
- Custom exceptions (ResourceNotFoundException, DuplicateRecordException)
- Proper HTTP status codes (404, 409, 400, 500)
- Detailed error messages with timestamps

---

## 🏗️ Architecture

### Layered Architecture (Modular Monolith Pattern)


Controller Layer          ← HTTP requests, @PreAuthorize security
↓
Service Layer            ← Business logic, calculations
↓
Repository Layer         ← Database queries (JPA)
↓
Entity/DTO Layer         ← Data models, transfer objects
↓
Database (MySQL)         ← Persistent storage


### Package Structure
com.TruelyFit.TruelyFit/
├── Config/               ← Spring configuration, exception handlers
├── Controller/           ← REST endpoints
├── Dto/                  ← Request/Response data transfer objects
├── Entity/               ← JPA entities
├── Enum/                 ← Enumerations (Role, Status, etc)
├── Exception/            ← Custom exceptions
├── Repository/           ← JPA repositories (data access)
└── Service/              ← Business logic services



### Database Schema (Relationships)
users (1) ←→ (1) trainers
users (1) ←→ (1) members
trainers (1) ←→ (Many) members
members (1) ←→ (Many) attendance
members (1) ←→ (Many) workout_plans
trainers (1) ←→ (Many) workout_plans
workout_plans (1) ←→ (Many) workout_exercises
members (1) ←→ (Many) diet_plans
trainers (1) ←→ (Many) diet_plans
diet_plans (1) ←→ (Many) diet_items


---

## 🛠️ Tech Stack

| Component | Technology | Version |
|-----------|-----------|---------|
| **Framework** | Spring Boot | 4.0.6 |
| **Java Version** | Java | 21.0.8 |
| **Language** | Java | Jakarta Persistence API |
| **Database** | MySQL | 8.0.36 |
| **ORM** | Hibernate | 7.2.12 |
| **Authentication** | JWT (JJWT) | 0.12.3 |
| **Password Encoding** | BCrypt | Spring Security Built-in |
| **API Documentation** | Swagger/OpenAPI | 2.3.0 |
| **Dependency Injection** | Lombok | 1.18.30 |
| **Build Tool** | Maven | 3.9+ |

---

## 🚀 Getting Started

### Prerequisites

- **Java 21+** — [Download](https://www.oracle.com/java/technologies/downloads/#java21)
- **Maven 3.9+** — [Download](https://maven.apache.org/)
- **MySQL 8.0+** — [Download](https://dev.mysql.com/downloads/mysql/)
- **Git** — [Download](https://git-scm.com/)

### Installation

1. **Clone the repository**
```bash
   git clone https://github.com/DIPAWALIMANDAOKA/TruelyFit-.git
   cd TruelyFit-
```

2. **Create MySQL database**
```sql
   CREATE DATABASE truelyfit;
```

3. **Update database credentials** in `src/main/resources/application.properties`
```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/truelyfit
   spring.datasource.username=root
   spring.datasource.password=your_password
```

4. **Build the project**
```bash
   mvn clean install
```

5. **Run the application**
```bash
   mvn spring-boot:run
```

6. **Access the application**
   - **API Base URL:** `http://localhost:8080`
   - **Swagger UI:** `http://localhost:8080/swagger-ui.html`
   - **API Docs JSON:** `http://localhost:8080/api-docs`

---

## 📚 API Endpoints

### Authentication


POST   /api/auth/register          Register new user
POST   /api/auth/login             Login and get JWT token



## 📚 API Endpoints

TrulyFit provides **35+ REST endpoints** across 6 major modules:
- **Authentication** (register, login)
- **Member Management** (CRUD operations)
- **Trainer Management** (CRUD operations)
- **Attendance Tracking** (mark attendance, history, summaries)
- **Workout Plans** (create, assign exercises, manage)
- **Diet Plans** (create, manage meals with macros)

**Full API documentation available at:**
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/api-docs`

### Example Request (Create Workout Plan)

```bash
curl -X POST http://localhost:8080/api/workout-plans \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "trainerId": 1,
    "memberId": 1,
    "name": "Strength Training - Week 1",
    "description": "Focus on compound movements",
    "startDate": "2026-05-05",
    "exercises": [
      {
        "exerciseName": "Bench Press",
        "sets": 4,
        "reps": 8,
        "weightKg": 80,
        "exerciseOrder": 1
      }
    ]
  }'
```

---



## 🔐 Security Features

### Role-Based Access Control

ADMIN    → Full system access, user management
TRAINER  → Create/manage workout & diet plans
MEMBER   → View own plans, attendance, profile


### JWT Token Flow
User registers/logs in
↓
Server issues JWT token (expires in 24 hours)
↓
Client sends token in Authorization header
↓
JwtAuthFilter validates token
↓
User authenticated + authorized for endpoint


### Example JWT Payload

```json
{
  "role": "ROLE_TRAINER",
  "sub": "trainer@trulyfit.com",
  "iat": 1777751064,
  "exp": 1777837464
}
```

---



## 📊 Database Schema

### Key Tables

**users** — Authentication & user info

id, name, email, password (BCrypt), role, created_at


**members** — Member profiles
id, user_id (FK), trainer_id (FK), goal, status, join_date, created_at

**trainers** — Trainer profiles
id, user_id (FK), specialisation, experience_years, bio, created_at

**attendance** — Daily attendance records
id, member_id (FK), attendance_date, status, marked_at, notes

**workout_plans** — Customized workout programs
id, trainer_id (FK), member_id (FK), name, description,
start_date, end_date, status, created_at

**workout_exercises** — Individual exercises in plans
id, workout_plan_id (FK), exercise_name, sets, reps,
weight_kg, duration_minutes, notes, exercise_order

**diet_plans** — Nutrition programs
id, trainer_id (FK), member_id (FK), name, description,
start_date, end_date, status, created_at

**diet_items** — Meals in diet plans
id, diet_plan_id (FK), meal_type, item_name, quantity,
calories, protein, carbs, fats, notes, item_order




---

## 💡 What I Learned Building This

1. **REST API Best Practices**
   - Proper use of HTTP methods and status codes
   - DTO pattern for request/response separation
   - Pagination, filtering, sorting patterns

2. **Spring Security**
   - JWT token generation and validation
   - Role-based authorization with `@PreAuthorize`
   - Method-level security

3. **Database Design**
   - One-to-Many and Many-to-One relationships
   - Foreign key constraints
   - Cascade delete operations
   - Query optimization with JPA repositories

4. **Exception Handling**
   - Global exception handler with `@ControllerAdvice`
   - Custom exceptions for business logic
   - Consistent error response format

5. **Clean Code**
   - Separation of concerns (Controller → Service → Repository)
   - Builder pattern for entity construction
   - Stream API for functional operations

---

## 🚀 Future Enhancements

- [ ] **AI Integration** — Claude API for personalized coaching tips
- [ ] **Progress Tracking** — Weight, measurements, strength progression
- [ ] **Mobile App** — React Native frontend
- [ ] **Payment Integration** — Stripe for membership plans
- [ ] **Notifications** — Email/SMS reminders for workouts
- [ ] **Analytics Dashboard** — Member progress reports
- [ ] **Docker & Kubernetes** — Containerization and orchestration
- [ ] **Unit Tests** — Mockito test suite (80%+ coverage)
- [ ] **CI/CD Pipeline** — GitHub Actions for automated testing

---

## 📈 Project Statistics

| Metric | Count |
|--------|-------|
| **Total Entities** | 8 |
| **API Endpoints** | 35+ |
| **Role Types** | 3 |
| **Features** | 6 Major |
| **Tables** | 8 |
| **Repository Methods** | 25+ |

---

## 🤝 Contributing

This is a portfolio project. Feel free to fork, improve, and submit PRs!

---

## 📞 Contact & Links

- **GitHub:** [github.com/DIPAWALIMANDAOKA](https://github.com/DIPAWALIMANDAOKA)
- **LinkedIn:** [linkedin.com/in/dipawali-mandaokar-99950020b](https://www.linkedin.com/in/dipawali-mandaokar-99950020b/)
- **Email:** dipawalimandaokar@gmail.com

---

## 📄 License

This project is open source and available under the MIT License.

---

## 🙏 Acknowledgments

- Spring Boot & Spring Security documentation
- JWT best practices guide
- MySQL Workbench for database design
- Postman for API testing

---

**Built with ❤️ by Dipawali Mandaokar**

*Last Updated: May 2026*














