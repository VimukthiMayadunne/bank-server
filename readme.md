# Bank Account Transfer Service

A simple Spring Boot service for transferring money between bank accounts with concurrency handling and transaction support.

## How to Run

1. **Clone the repository:**
   ```bash
   git clone https://github.com/your-username/bank-account-transfer-service.git

2. **Configure the database:** 
**src/main/resources/application.properties:**
   ```bash
   spring.datasource.url=jdbc:postgresql://localhost:5432/dbname
   spring.datasource.username=username
   spring.datasource.password=password
   
3. **Run the application using Maven:**
   ```bash
   mvn spring-boot:run
   
4. **The application will be available at http://localhost:8080**

## How to Run Tests

**Run Unit Tests using Maven:**
   ```bash
   mvn test
