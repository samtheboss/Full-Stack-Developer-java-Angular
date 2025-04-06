# Student Management System

## 📌 Overview

This system is built using:

- **Frontend:** Angular 18
- **Backend:** Spring Boot (Java 17+)
- **Database:** Microsoft SQL Server
- **Authentication:** JWT-based login (no registration)
- **File Operations:** Excel/CSV generation, image upload

---


---

## 🔐 Authentication

- Only `/login` endpoint is public.
- All other endpoints are protected via **JWT**.
- JWT is generated after successful login and stored in `localStorage`.
- Angular sends token via **HTTP Interceptor**.

---

## ⚙️ Backend Design

- **Spring Boot (RESTful API)**
- `@RestController`, `@Service`, `@Repository` pattern
- File read/write: Apache POI or EasyExcel
- Upload limits: 5MB (PNG/JPEG only)
- Secure endpoints via `OncePerRequestFilter`

---

## ⚡ Frontend Design

- Each feature (e.g., Login, Dashboard, Student Management) is isolated in its own **Angular Module**
- Uses **Reactive Forms**, **Route Guards**, and **HTTP Interceptors**
- Modular, maintainable, and scalable structure
- Supports lazy loading

---

## 📁 File Paths

- **Excel & CSV Files:**  
  `C:\var\log\applications\API\dataprocessing\`
- **Student Photos:**  
  `C:\var\log\applications\API\StudentPhotos\`

---

## 🧪 Test Users

- Test users are seeded automatically at backend startup.
- No registration form or user creation allowed.

---

## 🧰 Optimization Techniques

### ✅ Application-Level

- **Spring Boot**
  - Streamed writing for large files
    Used SXSSFWorkbook (streaming version of Apache POI) to efficiently handle writing large datasets (e.g., 1,000,000+ rows) without running out of memory. This streams data to temporary disk files instead of holding everything in memory.
  - Batch insert for database efficiency
  - DTO mapping via MapStruct
  - Connection pooling via HikariCP

- **Angular**
  - Lazy loaded feature modules
  - OnPush change detection
  - Efficient DOM rendering with `trackBy`
  - API response caching (where suitable)

### ✅ Database-Level

- **Indexes**
  - `studentId` (PK), `class`, `dob`
  - Filtered index on `status = 1`

- **Soft Delete**
  - Students marked inactive via `status = 0`

- **Scalability**
  - Partitioning by DOB or class (if dataset grows)
  - Proper connection pooling & transaction handling

---

## 📦 Features

### ✅ Login

- Login using username/password
- JWT returned and stored in localStorage

### ✅ Data Generation

- Generate 0 to 1M random student records
- Saves Excel file to configured folder

### ✅ Data Processing

- Converts Excel to CSV
- Adds +10 to scores
- Stores processed file in same folder

### ✅ Data Upload

- Reads Excel file, adds +5 to scores
- Uploads into DB (batch insert)

### ✅ Student Management

- View, Edit, Soft Delete
- Upload photo with 5MB/PNG-JPEG limit
- Rename image as `studentId-filename.ext`

### ✅ Student Report

- Paginated list of students
- Filters: studentId, class, DOB range
- Export filtered results to Excel

---

## 🚀 Setup Instructions

### 🔧 Backend

```bash
cd backend
./mvnw spring-boot:run

cd FrontEnd
npm install
ng serve

