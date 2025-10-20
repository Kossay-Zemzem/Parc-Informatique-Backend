# Parc Informatique Backend

This project is the backend service of a full-stack application developed during my summer internship that allows for managing IT equipment.

The backend provides RESTful APIs to manage and organize information related to the company's computers and laptops. It also serves the web front end at launch, which is built using Angular (See the [front-end Repository](https://github.com/Kossay-Zemzem/Parc-Informatique-Front) )

> ℹ️ A self-contained portable version of the application will be published later.

---

## ⚙️ Technologies & Tools

**Framework** : Spring Boot

**Dependencies**: Log4J, Lombok, [Apache POI](https://github.com/apache/poi), [OpenPDF](https://github.com/LibrePDF/OpenPDF)

**Database** : MySQL

**Tools** : Postman, Launch4J, Jlink  

---

## 🧭 How to Run

1. **Clone the repository**

   ```bash
   git clone https://github.com/Kossay-Zemzem/Parc-Informatique-Backend.git
   cd Parc-Informatique-Backend
   ```

2. **Set up the database**

   * Create a schema in MySQL :

     ```sql
     CREATE DATABASE DB_NAME;
     ```
   * Update `application.properties` with your database credentials:

     ```properties
     #Example : spring.datasource.url = jdbc:mysql://localhost:3306/parc_schema
     spring.datasource.url=jdbc:mysql://<HOST>:<PORT>/<DB_NAME>
     spring.datasource.username=<YOUR_USERNAME>
     spring.datasource.password=<YOUR_PASSWORD>
     ```

   * (Optional) Use environment variables for database credentials (username and password):

   * Example:
     ```properties
     spring.datasource.url=jdbc:mysql://<HOST>:<PORT>/<DB_NAME>
     spring.datasource.username=${MYSQL_DB_USER}
     spring.datasource.password=${MYSQL_DB_USER}
     ```
     
     ```bash
     export MYSQL_DB_USER=your_username
     export MYSQL_DB_PASSWORD=your_password
     ```

4. **Run the project**

   ```bash
   mvn spring-boot:run
   ```

---
## 🧱 Release version

Download : *(download link will be published soon)*

To run the software :

1- Unzip the file downloaded 

2- Configure database credentials in /config/application.properties 

2- Run `Parc Informatique.exe` 

---

## 🧪 Future Improvements

* Add authentication
* Fix some minor card overflow issues with smaller screens
* Add Docker support
* Add API documentation  

> ℹ️ The application was designed to run locally on Windows. The authentication and security module was deferred to a later phase due to time constraints. It will be integrated in a future release .


