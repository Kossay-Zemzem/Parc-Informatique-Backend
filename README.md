# Parc Informatique Backend

> [!NOTE]
> This project falls under an **academic context**, created during my **Second-Year Engineering Internship** in 2025. It serves as an archive of my learning journey in full stack developement. While this proejct represents a valuable learning experience for me, some techniques and implementation choices used may not reflect my current software engineering practices, Git workflows, architectural choices, or code quality standards.

## Project Overview

This repository contains the backend service for a full-stack IT equipment management application developed during my summer internship.

* **Web UI:** Manages and organizes company computers and laptops through a pre-compiled web interface served directly from the backend.
* **Database Setup:** App data is stored locally on your machine. The project uses MySQL, while the portable release uses an embedded H2 database located in `/data`.
* **System Tray Integration:** Features a desktop tray icon that lets you quickly open the interface in a browser or close the app.

For more details on the web UI, please refer to the [Frontend Repository](https://github.com/Kossay-Zemzem/Parc-Informatique-Front).

<img width="1920" height="984" alt="home" src="https://github.com/user-attachments/assets/ba554b53-cfd0-432a-9653-6e16d9a10807" />
<sub>*All data shown above is generic sample data used for demonstration purposes.</sub>

## App Features

- Add, modify, or delete IT equipment data such as PCs, laptops, and workstations.

- Filter IT equipment by location.

- Add, modify, or remove locations.

- Manage the history of each equipment item using date-sorted entries.

- Export equipment lists or history entries in 3 different formats (PDF, Excel, CSV).

- Recover deleted equipment data up to 120 days after deletion from the archive.

---
## Release Version

> [!IMPORTANT]
> An **internet connection** is currently required to run the application, as it's needed to use custom fonts from Google's API. This is an **unintended behavior**, full offline capability will be patched in a future release.

A self-contained portable version of the application is available for Windows users pre-loaded with sample data for quick testing.

Download: *(download link will be published soon)*

To run the software:

1. Unzip the downloaded file.
2. Run `Parc Informatique.exe`.
3. The web interface should automatically launch in your default browser at `http://localhost:8080/home`.

*If the web interface didn't launch automatically or if you disabled auto-launch, you can access it by right-clicking the app's tray icon and selecting **"Open App"** or by browsing to `http://localhost:8080/home`*

<img width="182" height="92" alt="Screenshot 2026-10-11 172638" src="https://github.com/user-attachments/assets/40caaefa-1f53-476d-ac0a-314782eecc81" />

### Customizing Auto-Launch

You can disable the auto-launch behavior on startup in `config/application.properties`

```properties
# Launch the browser automatically on startup (default: true)
app.autoLaunchBrowser=false
```

---
## Technologies & Tools

**Framework** : Spring Boot

**Dependencies**: Log4J, Lombok, [Apache POI](https://github.com/apache/poi), [OpenPDF](https://github.com/LibrePDF/OpenPDF)

**Database** : MySQL *(H2 was used for the portable release version)*

**Tools** : Postman, Launch4J, Jlink  

---

## Running Locally from Source code

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
## Known Bugs 
This is a list of known bugs that will be adressed in a future release:

- Application cannot function fully offline because custom fonts are loaded from Google Fonts's API.
- The "Create" button may overlap with other UI elements.
- Minor card overflow issues occur on smaller screens.

## Future Improvements
This entire project was developed by me in roughly 3 weeks, so there wasn't enough time to include everything in the initial release. More features can be added in the future including, but not limited to: 
* Add authentication support
* Add Docker support
* Add tooltips to action buttons to improve user experience.

## Data Privacy Notice
No confidential or proprietary company data is contained within this repository's source code, release binaries, or documentation. The company logo is publicly available branding. All test data used in this project is strictly mock data created for demonstration purposes and does not reflect real production environments.
