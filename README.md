# Customer Transaction Service

A simple Java Spring Boot REST API for managing customer transactions, developed for the Toucan Payments 2026 Fresher Engineering Challenge.

## Problem Understanding

The service manages transactions containing Transaction ID, Customer ID, Amount, Currency, Transaction Type, and Transaction Status.

The four required operations are:
1. Create a transaction
2. Get a transaction by Transaction ID
3. Update transaction status
4. Get all transactions for a Customer ID

The implementation includes validation, duplicate detection, business rules, exception handling, and automated tests.

## Technologies Used

Java 17, Spring Boot 3.5.5, Spring Web, Spring Data JPA, Spring Validation, H2, Maven, JUnit 5, MockMvc.

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
- Amount must be greater than zero, with a minimum of `0.01`.
- Currency must contain exactly three uppercase letters, such as `INR`, `USD`, or `EUR`.
- Transaction type must be a supported enum value.
- A newly created transaction must start with `PENDING`.
- `PENDING` can change to `COMPLETED`, `FAILED`, or `CANCELLED`.
- Final statuses cannot be changed again.

## API Endpoints

### 1. Create Transaction

**POST** `/api/transactions`

Example:
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

Example:
```json
{
  "status": "COMPLETED"
}
```

Returns `200 OK` for a valid transition and `400 BAD REQUEST` for an invalid transition.

### 4. Get Customer Transactions

**GET** `/api/customers/{customerId}/transactions`

Returns all transactions belonging to the specified customer.

## Error Handling

A global exception handler returns:
- `400 BAD REQUEST` for validation errors, invalid status transitions, and invalid initial status
- `404 NOT FOUND` when a transaction does not exist
- `409 CONFLICT` for duplicate transaction IDs
- `200 OK` for successful retrieval and update
- `201 CREATED` for successful creation

## Testing

The project uses Spring Boot Test and MockMvc.

The test suite contains 7 tests covering:
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

Run tests:
```text
.\mvnw.cmd clean test
```

Start the application:
```text
.\mvnw.cmd spring-boot:run
```

### Linux / macOS

Run tests:
```text
./mvnw clean test
```

Start the application:
```text
./mvnw spring-boot:run
```

The application runs on port `8080`.

## Database

The application uses an H2 in-memory database, so no external database installation is required. Transaction data is lost when the application stops.

## Known Limitations / Improvements

- H2 is an in-memory database; PostgreSQL or MySQL could be used for persistent storage.
- Authentication and authorization are not implemented because they are outside the assignment scope.
- Customer transaction retrieval does not use pagination or sorting.
- Response DTOs could be introduced instead of returning the entity directly.
- Currency validation checks the three-letter uppercase format rather than a supported-currency list.
- Additional edge-case tests, API documentation, structured logging, and database migrations could be added with more development time.

## Test Result

The complete test suite was run using:

```text
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

The required REST APIs were also manually tested using PowerShell.
