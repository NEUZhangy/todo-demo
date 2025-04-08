# Todo Demo Development Guide

## Architecture Overview

The application follows a standard 3-tier architecture with clear separation of concerns:

1. **Frontend**: React application (in `frontend/` directory)
   - Uses Axios for API calls to backend
   - State management via React hooks
   - Styled with CSS modules

2. **Backend**: Java Spring Boot application (in `backend_java/` directory)
   - REST API endpoints
   - JPA/Hibernate for database access
   - Spring Security for authentication (if configured)

3. **Database**: PostgreSQL (configured in docker-compose.yml)
   - ORM mapping via Hibernate
   - Connection pooling via HikariCP

```
┌─────────────┐    ┌───────────────────────┐    ┌─────────────┐
│  Frontend   │ ←→ │        Backend        │ ←→ │  Database   │
│ (React)     │    │ (Java Spring Boot)    │    │ (PostgreSQL)│
│             │    │ - Controllers         │    │             │
│ Components  │    │ - Services           │    │ Tables      │
│ State       │    │ - Repositories       │    │ Relations   │
└─────────────┘    └───────────────────────┘    └─────────────┘
```

## Detailed Development Guide

### Adding a New Database Table

1. **Model Class**:
```java
@Entity
@Table(name = "new_table")
public class NewTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    // Relationships
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    // Standard getters/setters
    // Consider using Lombok @Data if configured
}
```

2. **Repository Interface**:
```java
public interface NewTableRepository extends JpaRepository<NewTable, Long> {
    // Custom query methods
    List<NewTable> findByUser(User user);
    
    @Query("SELECT n FROM NewTable n WHERE n.createdAt > :date")
    List<NewTable> findRecent(@Param("date") LocalDateTime date);
}
```

3. **Database Schema Management**:
- Development: Hibernate auto-ddl (spring.jpa.hibernate.ddl-auto=update)
- Production: Use Flyway/Liquibase migrations:
```sql
-- V1__Create_new_table.sql
CREATE TABLE new_table (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP,
    user_id BIGINT REFERENCES users(id)
);
```

### Adding a New API (Java)

1. **Service Layer** (recommended):
```java
@Service
@Transactional
public class NewTableService {
    private final NewTableRepository repository;

    public NewTableService(NewTableRepository repository) {
        this.repository = repository;
    }

    public NewTable create(NewTable newTable) {
        return repository.save(newTable);
    }
    
    // Additional business logic
}
```

2. **Controller**:
```java
@RestController
@RequestMapping("/api/new-tables")
public class NewTableController {
    private final NewTableService service;

    public NewTableController(NewTableService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<NewTable>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @PostMapping
    public ResponseEntity<NewTable> create(@Valid @RequestBody NewTable newTable) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(service.create(newTable));
    }
}
```

3. **DTOs and Validation** (recommended):
```java
public class NewTableDto {
    @NotBlank
    private String name;
    
    // Getters/setters
}
```

### Development Workflow

1. **Local Development**:
```bash
# Start database only
docker-compose up -d db

# Run backend locally (with dev profile)
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

# Run frontend
cd frontend && npm start
```

2. **Testing**:
- Unit tests: `./mvnw test`
- Integration tests: Use `@SpringBootTest`
- API testing: Postman/curl examples provided

3. **Debugging**:
- Backend: Remote debug on port 5005
- Frontend: React DevTools
- Database: PGAdmin or DBeaver

4. **Production Deployment**:
```bash
# Build all services
docker-compose build

# Start in production mode
docker-compose up -d
```

