# Quick-Fix Guide: Critical Issues

This document provides step-by-step fixes for the most critical production-readiness issues.

## 1. Fix Exposed Credentials (IMMEDIATE - 30 minutes)

### Step 1: Remove Credentials from application.properties

**Current (INSECURE):**
```properties
spring.mail.username=nangdalet@gmail.com
spring.mail.password=wpup kkab ibes jyjb
telegram.api.token=7610150149:AAGpcooiR1ViLwi5Lw-asdfsaff
aba.apiKey=e6c0c473-7bb2-4021-aae0-90a307562312
```

**Fixed:**
```properties
spring.mail.username=${MAIL_USERNAME}
spring.mail.password=${MAIL_PASSWORD}
telegram.api.token=${TELEGRAM_API_TOKEN}
aba.apiKey=${ABA_API_KEY}
```

### Step 2: Set Environment Variables

```bash
# Linux/Mac
export MAIL_USERNAME="noreply@yourdomain.com"
export MAIL_PASSWORD="your_secure_password"
export TELEGRAM_API_TOKEN="7610150149:AAGpcooiR1ViLwi5Lw-xxxxxxxxxxxx"
export ABA_API_KEY="e6c0c473-7bb2-4021-aae0-90a307562312"

# Windows PowerShell
$env:MAIL_USERNAME="noreply@yourdomain.com"
$env:MAIL_PASSWORD="your_secure_password"
```

### Step 3: Regenerate Exposed Secrets

- Generate new Telegram API token
- Generate new ABA API key
- Update Gmail to use app-specific passwords
- Regenerate all hardcoded secrets

---

## 2. Fix CORS Configuration (HIGH - 15 minutes)

**Current (INSECURE):**
```properties
cors.allowedOrigins=*
```

**Fixed:**
```properties
cors.allowedOrigins=${CORS_ALLOWED_ORIGINS:http://localhost:3000,https://yourdomain.com}
```

Update in `src/main/java/com/dt/student/register/authentication/filter/CustomCorsFilter.java`:

```java
@Value("${cors.allowedOrigins}")
private String[] allowedOriginsConfig;

// This will now use environment variable or default
```

Then set environment variable:
```bash
export CORS_ALLOWED_ORIGINS="http://localhost:3000,https://yourdomain.com"
```

---

## 3. Fix SQL Logging in Production (MEDIUM - 5 minutes)

**Current (PERFORMANCE ISSUE):**
```properties
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

**Fixed (in application-prod.properties):**
```properties
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=false
```

**In application-dev.properties:**
```properties
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

---

## 4. Fix Management Endpoints Exposure (HIGH - 10 minutes)

**Current (INSECURE):**
```properties
management.endpoints.web.exposure.include=*
management.endpoint.health.show-details=always
```

**Fixed (in application-prod.properties):**
```properties
management.endpoints.web.exposure.include=health,info,metrics,prometheus
management.endpoint.health.show-details=when-authorized
```

---

## 5. Replace e.printStackTrace() with Logger (MEDIUM - 20 minutes)

**Current (BAD PRACTICE):**
```java
// In AuthServiceImpl.java line 100
} catch (Exception e) {
    e.printStackTrace();
    return ResponseMessageUtils.makeResponse(false, ...);
}
```

**Fixed:**
```java
private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

} catch (Exception e) {
    logger.error("Login failed for user: {}", loginRequest.getUsername(), e);
    return ResponseMessageUtils.makeResponse(false, ...);
}
```

Apply to all files:
- `AuthServiceImpl.java` (line 100)
- `UserServiceImpl.java` (line 138)

---

## 6. Create Environment-Specific Profiles (MEDIUM - 15 minutes)

Copy the provided configuration files to your project:

```bash
# Copy provided files
cp application-dev.properties src/main/resources/
cp application-staging.properties src/main/resources/
cp application-prod.properties src/main/resources/
cp src/main/resources/logback-dev.xml
cp src/main/resources/logback-staging.xml
cp src/main/resources/logback-prod.xml
```

Update main `application.properties` to activate profile:

```properties
spring.profiles.active=${SPRING_PROFILES_ACTIVE:dev}
```

### Launch with specific profile:

```bash
# Development
java -Dspring.profiles.active=dev -jar app.jar

# Production
java -Dspring.profiles.active=prod -jar app.jar

# Docker
docker run -e SPRING_PROFILES_ACTIVE=prod myapp:latest
```

---

## 7. Add Input Validation Annotations (MEDIUM - 30 minutes)

**Before (NO VALIDATION):**
```java
@PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
public ResponseMessage<BaseResult> login(@RequestBody LoginRequest loginRequest) {
    if (!username.isEmpty() && !password.isEmpty()) {
        // weak validation
    }
}
```

**After (PROPER VALIDATION):**
```java
@PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
public ResponseMessage<BaseResult> login(@Valid @RequestBody LoginRequest loginRequest) {
    // Framework validates automatically
}
```

Update `LoginRequest` DTO:

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be 3-50 characters")
    private String username;
    
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;
    
    @NotBlank(message = "Device ID is required")
    private String deviceId;
    
    @NotBlank(message = "Device name is required")
    private String deviceName;
}
```

---

## 8. Fix Incomplete Properties File (LOW - 2 minutes)

**Current (BROKEN):**
```properties
spring.datasource.hikari.
minimum-idle=2
```

**Fixed:**
```properties
spring.datasource.hikari.minimum-idle=5
```

---

## 9. Add Request Validation Exception Handler (MEDIUM - 15 minutes)

Already implemented in `GlobalExceptionHandler.java`, but ensure it's complete:

```java
@ExceptionHandler({MethodArgumentNotValidException.class})
public ResponseEntity<ResponseMessage<Object>> handleValidationExceptions(
        MethodArgumentNotValidException ex) {
    BindingResult result = ex.getBindingResult();
    List<FieldError> errors = result.getFieldErrors();
    
    Map<String, String> errorMap = new HashMap<>();
    for (FieldError error : errors) {
        errorMap.put(error.getField(), error.getDefaultMessage());
    }
    
    ResponseMessage<Object> response = ResponseMessageUtils.makeResponse(
            false, 
            HttpStatus.BAD_REQUEST.value(), 
            "VALIDATION_ERROR", 
            errorMap
    );
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
}
```

---

## 10. Fix Broken Dockerfile (LOW - 20 minutes)

Replace current `Dockerfile`:
```dockerfile
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY target/logistic-1.0.0.war app.war
EXPOSE 4800
ENTRYPOINT ["java", "-jar", "/app/app.war"]
```

With production-ready version: See `Dockerfile.prod`

### Build and test:
```bash
docker build -f Dockerfile.prod -t service-auth:1.0.0 .
docker run -e SPRING_PROFILES_ACTIVE=dev service-auth:1.0.0
```

---

## Implementation Checklist

### Phase 1: CRITICAL (Do Today - 2-3 hours)
- [ ] Move all credentials to environment variables
- [ ] Fix CORS configuration
- [ ] Remove SQL logging from production config
- [ ] Restrict management endpoints
- [ ] Regenerate exposed secrets
- [ ] Commit changes to git (add `.env` to `.gitignore`)

### Phase 2:HIGH (Do This Week - 2-3 hours)
- [ ] Replace all `e.printStackTrace()` with logging
- [ ] Create environment-specific profiles
- [ ] Add input validation annotations
- [ ] Fix incomplete properties
- [ ] Update Dockerfile
- [ ] Test all configurations

### Phase 3: MEDIUM (Complete Next Week - 2-3 hours)
- [ ] Add comprehensive error handling tests
- [ ] Setup centralized logging (ELK/Splunk)
- [ ] Implement request/response logging
- [ ] Add distributed tracing
- [ ] Document all changes

---

## Verification Commands

```bash
# Check if credentials are in code
grep -r "wpup kkab ibes jyjb" .
grep -r "7610150149" .
grep -r "e6c0c473" .

# Check if environment variables are used
grep -r "\${" src/main/resources/application*.properties

# Validate configuration
mvn spring-boot:run -Dspring-boot.run.arguments="test"

# Docker image scan for vulnerabilities
docker scan service-auth:1.0.0
```

---

## Timeline to Production-Ready

| Priority | Fixes | Effort | Timeline |
|----------|-------|--------|----------|
| CRITICAL | Fix credentials, CORS, logging | 2-3 hrs | TODAY |
| HIGH | Replace println, profiles, validation | 2-3 hrs | THIS WEEK |
| MEDIUM | Error handling, monitoring | 2-3 hrs | NEXT WEEK |
| LOW | Refactoring, optimization | 2-3 hrs | ONGOING |
| **TOTAL** | | **8-12 hrs** | **2 WEEKS** |

---

## Support Resources

- [12 Factor App - Configuration](https://12factor.net/config)
- [Spring Security Best Practices](https://spring.io/guides/gs/securing-web/)
- [OWASP Top 10](https://owasp.org/www-project-top-ten/)
- [Docker Security Best Practices](https://docs.docker.com/engine/security/)

---

Need help? Check the `PRODUCTION_READINESS_AUDIT.md` for detailed analysis.

