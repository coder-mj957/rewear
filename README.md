# ♻️ ReWear – Sustainable Second-Hand Fashion Marketplace

\<p align="center">   \<strong>Give pre-loved fashion a new life.\</strong> \</p>

\<p align="center">   \<a href="[https://rewear-black.vercel.app/](https://rewear-black.vercel.app/)">🌐 Live Website\</a> •   \<a href="[https://github.com/coder-mj957/rewear](https://github.com/coder-mj957/rewear)">💻 GitHub Repository\</a> \</p>

---

## 🌍 About the Project

**ReWear** is a web-based second-hand clothing marketplace designed to promote sustainable fashion by making it easier for people to buy and sell pre-owned clothing.

The platform provides a user-friendly shopping experience where customers can explore products, manage their shopping carts, place orders, and track their purchases. Administrators can manage products and monitor customer orders through an admin portal.

## ✨ Features

### 👤 User Features

- User registration and login
- Browse and search clothing products
- View detailed product information
- Add products to cart
- Update product quantities and remove cart items
- Buy products and place orders
- View order confirmation and order history
- View individual order details

### 🔐 Admin Features

- Admin portal and dashboard
- Add new products
- Edit existing products
- Delete products
- View and manage customer orders

### 🎨 UI Features

- Modern, responsive interface
- Light and dark mode
- Theme preference persistence
- Clean and user-friendly navigation

## 🛠️ Technologies Used

| Category        | Technology                  |
| --------------- | --------------------------- |
| Backend         | Java 21, Spring Boot        |
| Frontend        | HTML, CSS, Thymeleaf        |
| Database        | MySQL-compatible TiDB Cloud |
| ORM             | Spring Data JPA, Hibernate  |
| Build Tool      | Maven                       |
| Deployment      | Vercel                      |
| IDE             | IntelliJ IDEA               |
| Version Control | Git and GitHub              |

## 🚀 Live Demo

**Visit ReWear:** [https://rewear-black.vercel.app/](https://rewear-black.vercel.app/)

The application is deployed and accessible online.

## ⚙️ Run Locally

### Prerequisites

- Java JDK 21
- IntelliJ IDEA (recommended)
- MySQL or a compatible database
- Git

### Installation

1. Clone the repository:

   ```bash
   git clone https://github.com/coder-mj957/rewear.git
   ```

2. Open the project in IntelliJ IDEA.

3. Create a database named:

   ```sql
   CREATE DATABASE rewear_db;
   ```

4. Configure your database connection in `application-local.properties`:

   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/rewear_db
   spring.datasource.username=YOUR_USERNAME
   spring.datasource.password=YOUR_PASSWORD
   ```

5. Run the application using `RewearApplication.java`, or use:

   ```bash
   ./mvnw spring-boot:run
   ```

   On Windows:

   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

6. Open your browser:

   ```text
   http://localhost:8080
   ```

## 🔒 Environment Configuration

For deployment, configure the following environment variables in your hosting platform:

- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`
- `PORT`

**Important:** Never commit database passwords, private credentials, or other sensitive configuration files to GitHub.

## 📁 Project Structure

```text
rewear/
├── src/
│   └── main/
│       ├── java/
│       │   └── Rewear/
│       └── resources/
│           ├── static/
│           │   ├── css/
│           │   └── js/
│           ├── templates/
│           └── application.properties
├── pom.xml
├── Dockerfile
├── Dockerfile.vercel
├── vercel.json
└── README.md
```

## 🎯 Project Objective

ReWear aims to encourage sustainable consumption by extending the life cycle of clothing and making second-hand fashion more accessible.

## 👨‍💻 Author

**coder-mj957**

GitHub: [https://github.com/coder-mj957](https://github.com/coder-mj957)

---

⭐ If you like this project, consider giving the repository a star!
