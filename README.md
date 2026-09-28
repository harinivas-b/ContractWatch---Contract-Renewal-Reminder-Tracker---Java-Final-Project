# Contract Watch — Contract Renewal Reminder Tracker

A Spring Boot project for tracking vendor/service contracts, renewal notice periods, renewal decisions, and contract document references.

## Technology

- Java 17
- Spring Boot 3.4.5
- Spring Web / REST
- Spring Data JPA
- Jakarta Validation
- H2 file database by default
- MySQL driver included for MySQL configuration
- Spring Mail for optional SMTP reminders
- Maven

## Project structure

```text
com.contractwatch
├── controller
│   ├── ApiExceptionHandler.java
│   ├── ContractController.java
│   ├── DashboardController.java
│   └── VendorController.java
├── dto
│   ├── ContractRequest.java
│   ├── DocumentReferenceRequest.java
│   └── RenewalDecisionRequest.java
├── entity
│   ├── Contract.java
│   ├── ContractDocumentReference.java
│   ├── RenewalDecision.java
│   └── Vendor.java
├── repository
│   ├── ContractDocumentReferenceRepository.java
│   ├── ContractRepository.java
│   ├── RenewalDecisionRepository.java
│   └── VendorRepository.java
└── service
    ├── ContractNotFoundException.java
    ├── ContractService.java
    ├── ReminderScheduler.java
    ├── VendorNotFoundException.java
    └── VendorService.java
```

## Core features implemented

1. Add a vendor and store vendor information.
2. Add a contract with vendor, start date, end date, and renewal notice period.
3. Auto-flag a contract for renewal review once the current date reaches the notice-period boundary before the end date. The flag remains active through the end date while the contract is ACTIVE.
4. View all contracts expiring in the next 30 days.
5. Record a renewal decision as `RENEWED` or `TERMINATED`.
6. When renewed, a new end date is required and becomes the contract's current end date.
7. Terminated contracts are marked `TERMINATED`, their renewal review flag is cleared, and they are excluded from active renewal reminder queries.
8. Attach contract document references using URLs/links and view or remove those references.
9. Dashboard shows total contracts, active contracts, expiring contracts, and contracts in renewal review.
10. Daily scheduler checks ACTIVE contracts that are currently inside their configured renewal notice windows and can send an email reminder when SMTP is configured. Without SMTP, reminder activity is logged.

## Business rules enforced in the service layer

- Contract end date cannot be before the contract start date.
- Renewal notice period must be between 0 and 3650 days.
- The renewal review flag starts at `endDate - renewalNoticePeriodDays` and remains true through the end date while the contract is active.
- Contracts past their end date are automatically changed to `EXPIRED` during refresh operations.
- A renewed decision requires a new end date.
- A new end date cannot be before the current end date or the original contract start date.
- A terminated contract cannot remain in the active renewal reminder list.
- The expiring query accepts a window from 0 to 365 days.

## Run

1. Install Java 17 and Maven 3.9+ or open the folder in IntelliJ IDEA and let IntelliJ import the Maven project.
2. Open a terminal in the directory containing `pom.xml`.
3. Run:

```text
mvn spring-boot:run
```

4. Open:

```text
http://localhost:8080
```

## Default database

The project uses a file-based H2 database by default:

```text
JDBC URL: jdbc:h2:file:./data/contractwatch
Username: sa
Password: blank
```

H2 console:

```text
http://localhost:8080/h2-console
```

## REST endpoints

### Vendor

| Method | URL | Purpose |
|---|---|---|
| GET | `/api/vendors` | List vendors |
| POST | `/api/vendors` | Create vendor |

### Contract

| Method | URL | Purpose |
|---|---|---|
| GET | `/api/contracts` | List all contracts |
| GET | `/api/contracts/{id}` | Get one contract |
| POST | `/api/contracts` | Create contract |
| PUT | `/api/contracts/{id}` | Update contract |
| DELETE | `/api/contracts/{id}` | Delete contract |
| GET | `/api/contracts/expiring?days=30` | Contracts ending within N days |
| GET | `/api/contracts/renewal-review` | Contracts currently flagged for renewal review |
| POST | `/api/contracts/{id}/renewal-decision` | Record RENEWED / TERMINATED decision |
| GET | `/api/contracts/{id}/renewal-decisions` | View decision history |
| POST | `/api/contracts/{id}/documents` | Add a document reference |
| GET | `/api/contracts/{id}/documents` | View document references |
| DELETE | `/api/contracts/documents/{documentId}` | Delete a document reference |

### Dashboard

```text
GET /api/dashboard
```

Example response:

```json
{
  "totalContracts": 5,
  "activeContracts": 4,
  "expiringIn30Days": 2,
  "renewalReviewContracts": 1,
  "asOfDate": "2026-09-28"
}
```

## Example contract request

```json
{
  "vendorId": 1,
  "serviceName": "Annual printer maintenance",
  "contractStartDate": "2026-01-01",
  "endDate": "2026-12-31",
  "renewalNoticePeriodDays": 60,
  "contactEmail": "vendor@example.com",
  "status": "ACTIVE",
  "notes": "Review pricing before renewal"
}
```

For this example, the contract enters its renewal review window on 2026-11-01, exactly 60 days before the end date.

## Renewal decision request

### Renewed

```json
{
  "decision": "RENEWED",
  "newEndDate": "2027-12-31",
  "notes": "Renewed after annual pricing review"
}
```

### Terminated

```json
{
  "decision": "TERMINATED",
  "newEndDate": null,
  "notes": "Service no longer required"
}
```

## Document reference request

```json
{
  "title": "Signed Master Service Agreement",
  "referenceUrl": "https://example.com/contracts/msa-2026.pdf",
  "description": "Signed agreement stored in the organization's document system"
}
```

The prototype stores a reference/link; it does not upload or store the document file itself.

## Reminder scheduler

The scheduler runs daily at 9:00 AM using the server's configured timezone:

```text
@Scheduled(cron = "0 0 9 * * *")
```

It:

1. Refreshes renewal review flags.
2. Finds ACTIVE contracts whose current date is inside their configured renewal notice windows.
3. Uses contract email first and vendor email as a fallback.
4. Logs the reminder when SMTP is not configured.
5. Sends a simple email when SMTP is configured.

The current prototype does not persist sent-reminder history, so an active contract can be processed again on later days while it remains in the 30-day window.

## SMTP configuration

Use environment variables rather than committing passwords:

```text
SPRING_MAIL_HOST
SPRING_MAIL_PORT
SPRING_MAIL_USERNAME
SPRING_MAIL_PASSWORD
SPRING_MAIL_PROPERTIES_MAIL_SMTP_AUTH=true
SPRING_MAIL_PROPERTIES_MAIL_SMTP_STARTTLS_ENABLE=true
```

## MySQL configuration

The MySQL driver is already included in `pom.xml`. To use MySQL instead of H2, replace the datasource configuration in `application.properties` with environment-based values, for example:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/contract_watch
spring.datasource.username=${DB_USERNAME:root}
spring.datasource.password=${DB_PASSWORD:}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
```

Do not commit real passwords.

## What the jury should be able to see

- Vendor creation
- Contract creation with notice period
- Renewal review flag
- Expiring-in-30-days view
- Renewal decision history
- Renewed contract with a new end date
- Terminated contract excluded from active reminders
- Document reference links
- Dashboard counts
- Optional scheduler/email behavior
