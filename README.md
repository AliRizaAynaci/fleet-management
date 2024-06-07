# Fleet Management System

This project is a RESTful API that implements a basic fleet management system.
This API enables fleet management of shipments according to their delivery points.

## Project Overview

This project is designed to manage shipments within a logistics system. It includes functionality to handle different types of shipments, such as packages and sacks, and provides features for unloading these shipments at various delivery points. Below are the key concepts used in the project:

### Shipment Types:

- **Package:** Refers to a single item or good that is being shipped.
- **Sack:** Refers to a shipment type that consists of more than one item or good. A sack can contain multiple packages.

### Delivery Points:

There are three different delivery points in the system:

1. **Branch:** At branches, only packages can be unloaded. Sacks and packages in sacks cannot be unloaded.
2. **Distribution Center:** At distribution centers, sacks, packages in sacks, and packages that are not assigned to a sack can be unloaded.
3. **Transfer Center:** At transfer centers, only sacks and packages in sacks can be unloaded.

These concepts form the basis of the fleet management system implemented in this project.


## Technologies Used

- Java 17 : Main programming language
- Spring Boot : Framework for creating RESTful APIs
- Maven : Dependency management
- JUnit 5 : Testing framework
- Mockito : Mocking framework
- PostgreSQL : Database
- Docker : Containerization
- Swagger : API documentation

## Database Schema

### Package Table

| Field       | Type             | Description       |
|-------------|------------------|-------------------|
| id          | Long             | Primary Key       |
| barcode     | String           | Barcode           |
| deliveryPoint | Enum (String)   | Delivery Point    |
| state       | Enum (String)   | Shipment State    |
| desi        | Integer          | Desi              |
| sack        | Sack             | Sack              |

### Sack Table

| Field         | Type            | Description       |
|---------------|-----------------|-------------------|
| id            | Long            | Primary Key       |
| barcode       | String          | Barcode           |
| deliveryPoint | Enum (String)   | Delivery Point    |
| state         | Enum (String)   | Shipment State    |
| packages      | List\<Package\> | Packages          |


## Usage

### Running with Docker
This project can be easily run using Docker Compose. Follow the steps below:

1. Install Docker and Docker Compose: Make sure Docker and Docker Compose are installed on your system.
2. Starting with Docker Compose: Run the following command in the project's root directory:

   ```bash
   docker-compose up
    ```
3. Accessing the API: To access the API with a tool like Postman, you can use: `http://localhost:8080/v1/vehicles/{vehiclePlate}/distribute`.
4. Stopping Docker Compose: To stop the containers, run the following command:

   ```bash
   docker-compose down
    ```
### Running without Docker
If you prefer to run the project without Docker, follow the steps below:

1. Install PostgreSQL: Make sure PostgreSQL is installed on your system.
2. Create a Database: Create a database named `fleet_management_system`.
3. Update Database Configuration: Update the database configuration in the `application.properties` file.
4. Run the Application: Run the application using the following command:

   ```bash
   mvn spring-boot:run
    ```
5. Accessing the API: The API will be available at `http://localhost:8080/swagger-ui/index.html#/`.
6. Running Tests: Run the tests using the following command:

   ```bash
   mvn test
    ```
7. Stopping the Application: Stop the application by pressing `Ctrl + C`.
8. Stopping PostgreSQL: Stop the PostgreSQL service on your system.
9. Deleting the Database: Delete the `fleet_management_system` database.


## Distribution Endpoint

- **URL:** `/api/v1/vehicles/{vehiclePlate}/distribute`
- **Method:** POST
- **Description:** Distributes a distribution request to a vehicle.
  - **Request Body:** JSON
    ```json
    {
      "route": [
        {
          "deliveryPoint": "BRANCH",
          "deliveries": [
            {"barcode": "P7988000121"},
            {"barcode": "P7988000122"},
            {"barcode": "P7988000123"},
            {"barcode": "P8988000121"},
            {"barcode": "C725799"}
          ]
        }
      ]
    }

      ```

  - **Response Body:** JSON
    ```json
    {
      "vehicle": "vehiclePlate",
      "route": [
        {
          "deliveryPoint": "BRANCH",
          "deliveries": [
            {"barcode": "P7988000121", "state": "UNLOADED"},
            {"barcode": "P7988000122", "state": "UNLOADED"},
            {"barcode": "P7988000123", "state": "UNLOADED"},
            {"barcode": "P8988000121", "state": "LOADED"},
            {"barcode": "C725799", "state": "LOADED"}
          ]
        }
      ]
    }
    ```

## API Documentation

The API documentation is available at `http://localhost:8080/swagger-ui/index.html`.


