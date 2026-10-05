# Contract Watch

Contract Watch is a Spring Boot project for maintaining vendor contracts, renewal dates, renewal decisions, and contract document references.

## Technology
- Java 17
- Spring Boot 3.4.5
- Spring Web / REST
- Spring Data JPA
- MySQL
- Maven

## Code structure
The project follows the same simple structure and coding style as the supplied reference project:

```text
com.contractwatch
├── Controller
├── Entity
├── Repository
└── Service
```

No DTO layer, custom exception layer, scheduler, mail service, or extra framework concepts are used.

## Main modules
- Vendor management
- Contract management
- Renewal review
- Renewal decision history
- Contract document references
- Dashboard summary

## Database
The application uses MySQL with database name `contract_watch`.

Default configuration:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/contract_watch?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=
```

Change the username and password for the local MySQL installation.

## Run
Open the folder containing `pom.xml` in IntelliJ IDEA and run `ContractWatchApplication`.

Then open:
`http://localhost:8080`

## API endpoints
### Vendor
- POST `/api/vendors/create`
- GET `/api/vendors/getVendor`
- GET `/api/vendors/getVendor/{id}`
- PUT `/api/vendors/update/{id}`
- DELETE `/api/vendors/delete/{id}`

### Contract
- POST `/api/contracts/create`
- GET `/api/contracts/getContract`
- GET `/api/contracts/getContract/{id}`
- PUT `/api/contracts/update/{id}`
- DELETE `/api/contracts/delete/{id}`
- GET `/api/contracts/expiring?days=30`
- GET `/api/contracts/renewal-review`
- POST `/api/contracts/{id}/renewal-decision`
- GET `/api/contracts/{id}/renewal-decisions`
- POST `/api/contracts/{id}/documents`
- GET `/api/contracts/{id}/documents`
- DELETE `/api/contracts/documents/{documentId}`

### Dashboard
- GET `/api/dashboard`

## Notes
The project is implemented as a software prototype. It does not add physical hardware, IoT sensors, GIS, or automatic pump control concepts.
