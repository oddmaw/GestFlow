# Order Management System

## Project Overview

This project is a desktop application designed to manage orders and invoices for a small business. It allows users to keep track of their products, clients, sales invoices, and internal product requests (commands/orders). The application is built using Java and a MySQL database for data persistence.

## Key Features

*   **Product Management:** Add, modify, delete, and view products (name, price, quantity).
*   **Client Management:** Add, modify, delete, and view clients (name, email, phone).
*   **Invoice Management:** Generate invoices, associating them with clients and products with their respective quantities, including subtotal and total calculations.
*   **Command/Order Management:** Create internal product requests (commands), associating them with clients and products, tracking quantities, and calculating total costs.
*   **Data Persistence:** All data is stored in a MySQL database.

## Technologies Used

*   **Programming Language:** Java
*   **Database:** MySQL

## Project Structure

The project is organized using a layered architecture:


*   **`src/`**:
*    *`database`:* Contains the `DatabaseConnection.java` file, responsible for establishing a connection to the MySQL database.
*    *`models`:* Data model classes representing entities in the database (e.g., `Product`, `Client`, `Invoice`, `Command`, `LineItem`).
*    *`dao`:* Data Access Object classes responsible for interacting with the database (e.g., `ProductDAO`, `ClientDAO`, `InvoiceDAO`, `CommandDAO`).
*    *`services`:* Classes containing the business logic of the application, acting as an intermediary between the UI and DAOs (e.g., `ProductService`, `ClientService`, `InvoiceService`, `CommandService`).
*    *`ui`:* This folder will hold the JavaFX user interface related code.

*   **`UML`:** (Optional) Contains UML diagrams if you made them.
*   **`lib`:**  Contains external library `.jar` files. Currently containing the MySQL Connector/J library (`mysql-connector-java.jar`).
*   **`export`:** Contains export files such as PDFs and CSVs.


## Setting Up the Project

Follow these steps to set up the project locally:

### 1. Prerequisites

*   **Java Development Kit (JDK):** Make sure you have Java 11 or higher installed on your system. You can download it from [Oracle's website](https://www.oracle.com/java/technologies/downloads/).
*   **MySQL Database:** You need to have MySQL server installed and running. You can download it from the [MySQL website](https://dev.mysql.com/downloads/mysql/).

### 2. Clone the Repository

Clone the project repository to your local machine using Git:

```bash
git clone https://github.com/oddmaw/GestFlow.git