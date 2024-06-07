# Fleet Management System

This project is a RESTful API that implements a basic fleet management system.
This API enables fleet management of shipments according to their delivery points.

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

### Shipment Table

| Field         | Type            | Description       |
|---------------|-----------------|-------------------|
| id            | Long            | Primary Key       |
| barcode       | String          | Barcode           |
| deliveryPoint | Enum (String)   | Delivery Point    |
| state         | Enum (String)   | Shipment State    |

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
            {"barcode": "barcode1"},
            {"barcode": "barcode2"},
            {"barcode": "barcode3"},
            {"barcode": "barcode4"},
            {"barcode": "barcode5"}
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
            {"barcode": "barcode1", "state": "UNLOADED"},
            {"barcode": "barcode2", "state": "UNLOADED"},
            {"barcode": "barcode3", "state": "UNLOADED"},
            {"barcode": "barcode4", "state": "LOADED"},
            {"barcode": "barcode5", "state": "LOADED"}
          ]
        }
      ]
    }
    ```

## API Documentation

The API documentation is available at `http://localhost:8080/swagger-ui/index.html`.


