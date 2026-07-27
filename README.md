# Banking Transaction Engine

A production-ready backend system that simulates core banking transaction processing with regulatory-compliant validation, fraud detection, and audit logging.

Built with Java Spring Boot, implementing Chain of Responsibility pattern for transaction validation — mirroring how real Australian banks process and validate financial transactions.

## Architecture

```
Client (REST API)
      │
      ▼
┌─────────────┐
│  Controller  │  ── REST endpoints, request handling
└──────┬──────┘
       │
       ▼
┌─────────────┐
│   Service    │  ── Business logic, transaction processing
└──────┬──────┘
       │
       ▼
┌─────────────────────────────────────────────────────┐
│              Validation Chain (CoR)                  │
│                                                     │
│  AccountStatus → Balance → DailyLimit →             │
│  DestinationAccount → Fraud (TTR) →                 │
│  WithdrawalLimit → StructuringDetector              │
└──────┬──────────────────────────────────────────────┘
       │
       ▼
┌─────────────┐     ┌─────────────┐
│ Repository   │────▶│ PostgreSQL  │
└─────────────┘     └─────────────┘
```

## Features

**Transaction Processing**
- Deposit, withdrawal, and transfer between accounts
- Real-time balance updates with BigDecimal precision
- Transaction history per account

**Chain of Responsibility Validation**

Every transaction passes through 7 validators before approval:

| Validator | Rule | Reference |
|---|---|---|
| AccountStatusValidator | Reject if account is frozen | Bank internal policy |
| BalanceValidator | Reject if insufficient funds (skip for deposits) | Bank internal policy |
| DailyLimitValidator | Reject if single transaction > $20,000 | Bank internal policy |
| DestinationAccountValidator | Reject transfer if destination account not found | Bank internal policy |
| FraudValidator | Flag transactions >= $10,000 for review | AUSTRAC TTR — AML/CTF Act 2006 s43 |
| WithdrawalLimitValidator | Reject if > 6 withdrawals/month (Savings only) | Regulation D convention |
| StructuringDetector | Flag if 24h cumulative >= $10,000 with individual transactions < $10,000 | AUSTRAC SMR — anti-structuring |

**Audit Trail**
- Every account creation, transaction, and interest calculation is logged
- Compliance-ready: all balance changes have an audit record
- Query audit history per account

**Interest Calculation**
- Savings accounts: monthly interest based on annual rate (default 4.5%)
- Checking accounts: no interest applied

**Error Handling**
- Global exception handler returns structured JSON errors
- Proper HTTP status codes (400 Bad Request, 404 Not Found)

## Tech Stack

- **Backend:** Java 17, Spring Boot 4.1
- **Database:** PostgreSQL 16 (Docker), H2 (development)
- **ORM:** Spring Data JPA / Hibernate
- **Testing:** JUnit 5, Mockito (29 tests)
- **DevOps:** Docker, Docker Compose, GitHub Actions CI/CD
- **Design Pattern:** Chain of Responsibility

## API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/accounts?ownerName={name}&accountType={type}` | Create account |
| GET | `/api/accounts/{id}` | Get account by ID |
| GET | `/api/accounts/number/{accountNumber}` | Get account by number |
| POST | `/api/accounts/interest?accountNumber={number}` | Calculate monthly interest |
| POST | `/api/transaction?amount={}&type={}&sourceAccount={}&destinationAccount={}` | Process transaction |
| GET | `/api/transaction/history/{accountNumber}` | Get transaction history |
| GET | `/api/audit/{accountNumber}` | Get audit trail |

## Quick Start

### Local Development (H2)

```bash
git clone https://github.com/NhatAnh279/banking-engine.git
cd banking-engine
./mvnw spring-boot:run
```

App runs at `http://localhost:8080`

### Docker (PostgreSQL)

```bash
docker compose up --build
```

Creates Spring Boot app + PostgreSQL database with persistent storage.

### Run Tests

```bash
./mvnw test
```

## Testing

29 unit tests covering core business logic:

- **Validation layer** — all 7 validators tested with boundary cases and edge cases
- **Service layer** — deposit, withdrawal, transfer, interest calculation flows
- **Mockito** — repository mocking for isolated unit testing

Test categories:
- Happy path (valid transactions)
- Rejection cases (insufficient balance, frozen account, daily limit)
- Fraud detection (TTR threshold, structuring patterns)
- Boundary testing (exact threshold amounts)
- Error cases (account not found)

## CI/CD Pipeline

```
Push to main
    │
    ▼
GitHub Actions
    │
    ├── Build + Test (Maven)
    │
    └── Build + Push Docker Image (Docker Hub)
```

Automated on every push to `main`:
1. Compile and run 29 unit tests
2. Build Docker image with multi-stage build
3. Push to Docker Hub

## Project Structure

```
src/main/java/com/tommy/bankingengine/
├── controller/        API endpoints
│   ├── AccountController.java
│   ├── TransactionController.java
│   └── AuditLogController.java
├── service/           Business logic
│   ├── AccountService.java
│   ├── TransactionService.java
│   ├── InterestService.java
│   └── AuditLogService.java
├── validation/        Chain of Responsibility
│   ├── TransactionValidator.java (interface)
│   ├── AccountStatusValidator.java
│   ├── BalanceValidator.java
│   ├── DailyLimitValidator.java
│   ├── DestinationAccountValidator.java
│   ├── FraudValidator.java
│   ├── WithdrawalLimitValidator.java
│   └── StructuringDetector.java
├── model/             JPA entities
│   ├── Account.java
│   ├── Transaction.java
│   └── AuditLog.java
├── repository/        Data access
│   ├── AccountRepository.java
│   ├── TransactionRepository.java
│   └── AuditLogRepository.java
└── exception/         Error handling
    └── GlobalExceptionHandler.java
```

## Roadmap

- [ ] ML-based fraud scoring microservice (Python FastAPI + XGBoost)
- [ ] Cloud deployment (Railway)
- [ ] Swagger/OpenAPI documentation
- [ ] Request/Response DTOs
- [ ] Spring Security authentication
