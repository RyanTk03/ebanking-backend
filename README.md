# Lab Report: E-Banking Backend Application

**Frameworks: Spring Boot, Data JPA, Spring Security & OAuth2**

---

## 1. Project Objectives

The objective is to design and implement a secure RESTful backend for an e-banking system to:

- Manage bank accounts and financial operations (CRUD).
- Expose a secured REST API consumable by frontend clients.
- Protect resources using JWT-based authentication via OAuth2 Resource Server.
- Apply role-based access control to sensitive endpoints.

---

## 2. Technical Architecture

The project is based on a layered (N-Tier) architecture:

1. **Presentation Layer**: REST Controllers (Spring Web MVC).
2. **Business / Service Layer**: Application services handling banking logic.
3. **Data Access Layer (DAO)**: Spring Data JPA.
4. **Security Layer**: Spring Security + OAuth2 Resource Server (JWT).
5. **Database**: MariaDB.

---

## 3. Domain Model

The core entities represent the banking domain. **Lombok** is used for automatic generation of boilerplate code (getters, setters, constructors, builders).

```java
@Entity
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class BankAccount {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountNumber;
    private double balance;
    private AccountType type; // CURRENT or SAVING

    @ManyToOne
    private Customer customer;

    @OneToMany(mappedBy = "bankAccount", fetch = FetchType.LAZY)
    private List<AccountOperation> operations;
}
```

---

## 4. Data Access Layer (DAO & Repository)

Spring Data JPA provides automatic CRUD implementations through repository interfaces.

```java
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
    List<BankAccount> findByCustomerId(Long customerId);
}

public interface AccountOperationRepository extends JpaRepository<AccountOperation, Long> {
    Page<AccountOperation> findByBankAccountId(Long accountId, Pageable pageable);
}
```

---

## 5. REST API & Controllers

Controllers expose the banking operations over HTTP. Pagination is supported for operation history endpoints.

**Key endpoints:**

| Method | Endpoint | Description |
| --- | --- | --- |
| GET | `/api/customers` | List all customers |
| POST | `/api/customers` | Create a customer |
| GET | `/api/accounts/{id}` | Get account details |
| POST | `/api/accounts/{id}/debit` | Debit an account |
| POST | `/api/accounts/{id}/credit` | Credit an account |
| POST | `/api/accounts/transfer` | Transfer between accounts |
| GET | `/api/accounts/{id}/operations` | Paginated operation history |

```java
@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class BankAccountController {

    private final BankAccountService bankAccountService;

    @GetMapping("/{accountId}/operations")
    public Page<AccountOperationDTO> getAccountHistory(
            @PathVariable Long accountId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return bankAccountService.getAccountHistory(accountId, page, size);
    }
}
```

---

## 6. Application Security

The API is protected using **Spring Security** combined with **OAuth2 Resource Server** for stateless JWT-based authentication. Access is controlled by user roles.

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(ar -> ar
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
            .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

**Roles:**

- `USER`: Read-only access (consult accounts and operation history).
- `ADMIN`: Full access (create accounts, perform debit/credit/transfer operations).

---

## 7. Technologies & Dependencies

| Technology | Version | Role |
| --- | --- | --- |
| Java | 17  | Language |
| Spring Boot | 4.0.6 | Application framework |
| Spring Data JPA | (managed) | ORM / Data access |
| Spring Security | (managed) | Authentication & Authorization |
| Spring OAuth2 Resource Server | (managed) | JWT token validation |
| Spring Web MVC | (managed) | REST API |
| MariaDB | (runtime) | Relational database |
| Lombok | (optional) | Boilerplate reduction |
| Maven | Wrapper | Build tool |

---

## 8. Getting Started

### Prerequisites

- Java 17+
- MariaDB running locally (or Docker)
- Maven (or use the included `./mvnw` wrapper)

### Configuration

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mariadb://localhost:3306/ebanking
spring.datasource.username=your_user
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update

# JWT configuration
spring.security.oauth2.resourceserver.jwt.secret=your_jwt_secret
```

### Run

```bash
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080`.

---

## 9. Conclusion

This lab demonstrated the integration of key Spring ecosystem components to build a production-grade banking backend. Spring Data JPA simplifies data persistence, Spring MVC exposes a clean REST API, and the combination of Spring Security with OAuth2 Resource Server provides stateless, token-based security adapted to modern distributed architectures.
