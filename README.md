# Customer Transaction Service

A simple Java Spring Boot REST API for managing customer transactions, developed for the Toucan Payments 2026 Fresher Engineering Challenge.

## Problem Understanding

The service manages transactions containing:

- Transaction ID
- Customer ID
- Amount
- Currency
- Transaction Type
- Transaction Status

It implements the four required operations:

1. Create a transaction
2. Get a transaction by Transaction ID
3. Update transaction status
4. Get all transactions for a Customer ID

The implementation focuses on validation, duplicate detection, business rules, error handling, and meaningful automated tests.

## Technologies Used

- Java 17
- Spring Boot 3.5.5
- Spring Web
- Spring Data JPA
- Spring Validation
- H2 Database
- Maven
- JUnit 5
- MockMvc

## Project Structure

```text
src/main/java/com/example/transactionstarter

├── controller
│   └── TransactionController.java
├── dto
│   ├── CreateTransactionRequest.java
│   └── UpdateStatusRequest.java
├── entity
│   └── Transaction.java
├── exception
│   ├── DuplicateTransactionException.java
│   ├── InvalidStatusTransitionException.java
│   ├── TransactionNotFoundException.java
│   └── GlobalExceptionHandler.java
├── model
│   ├── TransactionStatus.java
│   └── TransactionType.java
├── repository
│   └── TransactionRepository.java
├── service
│   └── TransactionService.java
└── TransactionStarterApplication.java
```

## Assumptions and Validation Rules

- Transaction ID and Customer ID are required and cannot be blank.
- Transaction ID must be unique.
- Amount must be greater than zero, with a minimum value of `0.01`.
- Currency must contain exactly three uppercase letters, such as `INR`, `USD`, or `EUR`.
- Transaction type must be a supported enum value.
- A newly created transaction must start with `PENDING`.
- A transaction can move from `PENDING` to `COMPLETED`, `FAILED`, or `CANCELLED`.
- Final statuses cannot be changed again.

These rules keep transaction data valid and make status changes predictable.

## API Endpoints

### 1. Create Transaction

**POST** `/api/transactions`

Example request:

```json
{
  "transactionId": "TXN1001",
  "customerId": "CUS1001",
  "amount": 1500.00,
  "currency": "INR",
  "transactionType": "PAYMENT",
  "status": "PENDING"
}
```

Returns `201 CREATED` when successful.

### 2. Get Transaction

**GET** `/api/transactions/{transactionId}`

Returns `200 OK` when found and `404 NOT FOUND` when the transaction does not exist.

### 3. Update Transaction Status

**PATCH** `/api/transactions/{transactionId}/status`

Example request:

```json
{
  "status": "COMPLETED"
}
```

Returns `200 OK` for a valid transition and `400 BAD REQUEST` for an invalid transition.

### 4. Get Customer Transactions

**GET** `/api/customers/{customerId}/transactions`

Returns all transactions belonging to the specified customer.

## Status Transition Rules

```text
PENDING → COMPLETED
PENDING → FAILED
PENDING → CANCELLED
```

Once a transaction reaches a final status, it cannot be changed again. This prevents already-finalized transactions from being modified accidentally.

## Error Handling

A global exception handler provides consistent HTTP responses:

| Situation | HTTP Status |
|---|---:|
| Transaction created | 201 CREATED |
| Successful retrieval/update | 200 OK |
| Transaction not found | 404 NOT FOUND |
| Duplicate Transaction ID | 409 CONFLICT |
| Validation failure | 400 BAD REQUEST |
| Invalid status transition | 400 BAD REQUEST |
| Invalid initial status | 400 BAD REQUEST |

## Testing

The project uses Spring Boot Test and MockMvc.

The current test suite contains 7 tests covering:

1. Successful transaction creation
2. Validation failure
3. Duplicate Transaction ID
4. Non-existent transaction
5. Successful status update
6. Customer transaction retrieval
7. Spring application context loading

The H2 database is used for tests and transaction data is cleared before each test.

## How to Run

### Windows

Run the tests:

```powershell
.\mvnw.cmd clean test
```

Start the application:

```powershell
.\mvnw.cmd spring-boot:run
```

### Linux / macOS

Run the tests:

```bash
./mvnw clean test
```

Start the application:

```bash
./mvnw spring-boot:run
```

The application runs on port `8080`.

The root URL `/` is not mapped because no root endpoint is required. The required APIs are available under `/api`.

## Database

The application uses an H2 in-memory database, so no external database installation is required.

Transaction data is not persisted after the application stops.

## Known Limitations

- H2 is an in-memory database, so data is lost when the application stops.
- Authentication and authorization are not implemented because they are outside the assignment scope.
- Customer transaction retrieval does not currently use pagination or sorting.
- Response DTOs could be introduced instead of returning the entity directly.
- Currency validation checks the three-letter uppercase format rather than maintaining a separate supported-currency list.

## Future Improvements

With more development time, I would consider:

- Persistent database support such as PostgreSQL or MySQL
- Response DTOs
- Pagination and sorting
- Additional edge-case tests
- OpenAPI/Swagger documentation
- Structured logging and monitoring
- Database migrations using Flyway or Liquibase

## Test Result

The complete test suite was run using:

```powershell
.\mvnw.cmd clean test
```

Result:

```text
Tests run: 7
Failures: 0
Errors: 0
Skipped: 0
BUILD SUCCESS
```

The application was also manually tested through the REST APIs using PowerShell, including transaction creation, retrieval, status update, and customer transaction retrieval.
