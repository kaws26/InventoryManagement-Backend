# 🧠 Inventory Management System – Backend

This is the **Backend** of the Inventory Management System, built with **Spring Boot** and **MySQL**. It provides a secure and scalable API that supports full **CRUD operations** for managing users, products, categories, and transactions.

> ✅ Fully integrated with a React.js frontend  
> 🔐 Includes Role-based Authentication for Admin and Manager  
> 🛒 Handles sell/purchase transactions with real-time inventory updates

---

## 🚀 Features

- 🔐 **Role-Based Authentication** (Admin, Manager)
- 👤 User Management (Create, Update, Delete, View)
- 📦 Product Inventory with auto quantity tracking
- 🗂️ Category Management
- 🔁 Purchase & Sell Transactions
- 📊 Automatic inventory updates based on transaction type
- 📄 RESTful API structure
- 🧩 Integrated with MySQL Database
- 🔗 CORS enabled for frontend (React)

---

## 🛠️ Tech Stack

| Tech             | Description                                 |
|------------------|---------------------------------------------|
| Spring Boot      | Java-based backend framework                |
| Spring Security  | For authentication and role-based access    |
| MySQL            | Relational database                         |
| JPA / Hibernate  | ORM for DB operations                       |
| Lombok           | Reduces boilerplate code                    |
| Maven            | Dependency management                       |

---

## 🧾 Modules Overview

### 👤 User Module
- Add / Update / Delete users
- Login with JWT token
- Role: Admin / Manager

### 📦 Product Module
- CRUD operations on inventory items
- Track quantity in real-time

### 🗂️ Category Module
- Manage product categories

### 🔁 Transaction Module
- Record purchase / sell transactions
- Automatically updates product quantity
- List transaction history

---

## 🔗 API Endpoints Overview

### Auth Endpoints
```

POST   /api/auth/login
POST   /api/auth/register

```

### User Management (Admin only)
```

GET    /api/users
POST   /api/users
PUT    /api/users/{id}
DELETE /api/users/{id}

```

### Product Management
```

GET    /api/products
POST   /api/products
PUT    /api/products/{id}
DELETE /api/products/{id}

```

### Category Management
```

GET    /api/categories
POST   /api/categories
PUT    /api/categories/{id}
DELETE /api/categories/{id}

```

### Transaction Management
```

GET    /api/transactions
POST   /api/transactions/purchase
POST   /api/transactions/sell

````

---

## 🧰 Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/your-username/InventoryManagement-Backend.git
cd InventoryManagement-Backend
````

### 2. Configure the Database

Edit `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/inventory_db
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

> ✅ Make sure MySQL is running and the database `inventory_db` is created.

### 3. Run the Application

```bash
./mvnw spring-boot:run
```

The backend will run at:
`http://localhost:8080`

---

## 🔐 Authentication and Roles

* Authentication is handled via **JWT Token**.
* Roles:

  * `ROLE_ADMIN`: Full access
  * `ROLE_MANAGER`: Limited to product and transaction access

---

## 🧑‍💻 Author

**Kawaljeet Singh**
B.Tech IT, USICT

> Passionate backend developer with hands-on experience in Spring Boot and secure full-stack systems.

---

## 📄 License

This project is licensed under the [MIT License](LICENSE).

---

## 📦 Connect With Frontend

* React.js Frontend: [Link to Frontend Repo](https://github.com/kaws26/InventoryManagement-Frontend)
* Ensure CORS config allows requests from the frontend origin.

---

