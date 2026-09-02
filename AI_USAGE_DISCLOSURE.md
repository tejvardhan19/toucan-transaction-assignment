# AI Usage Disclosure

## Tools Used

I used ChatGPT and GitHub Copilot in Visual Studio Code as AI coding assistants while completing this assignment.

## How I Used AI

I mainly used ChatGPT to understand the requirements, discuss a simple Spring Boot structure, and get guidance while implementing the transaction entity, DTOs, repository, service, controller, exception handling, and tests.

I used GitHub Copilot in Visual Studio Code for coding assistance while writing and refining parts of the implementation.

I also used AI assistance to understand validation rules, HTTP status codes, and to troubleshoot some setup and runtime issues while working with the provided starter project.

## Significant AI Suggestions

The AI tools provided suggestions and examples for the transaction model, REST endpoints, service-layer business logic, custom exceptions, global exception handling, and JUnit/MockMvc tests.

I kept the implementation simple and focused on the four operations required by the assignment rather than adding unnecessary features.

## What I Changed and Verified

I reviewed the suggestions and adapted them to the provided starter project. I checked the package structure, API paths, validation rules, status transition logic, and database interaction.

I did not assume that AI-generated code was automatically correct. I ran the tests and also manually tested the REST APIs using PowerShell.

One issue I had to verify was the Whitelabel 404 page when opening `/` in the browser. I confirmed that this was expected because my application does not define a `/` endpoint; the required APIs are under `/api`.

## How I Verified the Result

I ran the complete Maven test suite and got:

- Tests run: 7
- Failures: 0
- Errors: 0
- Skipped: 0
- BUILD SUCCESS

I also started the application and manually tested creating a transaction, retrieving it, updating its status, and retrieving transactions for a customer.

## Responsibility

I worked on the main implementation and understand the submitted code. I used AI mainly for guidance and learning, and I reviewed and tested the final implementation myself.