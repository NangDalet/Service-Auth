# Environment Variables Configuration Guide

This document lists all environment variables required for ServiceAuth microservice in different environments.

## Required Environment Variables

### Database Configuration (CRITICAL)
```bash
DB_URL=jdbc:mysql://hostname:3306/student_register?useSSL=true&serverTimezone=UTC
DB_USERNAME=db_user
DB_PASSWORD=secure_password_here      # ✅ NEVER hardcode
```

### Security Configuration (CRITICAL)
```bash
JWT_CLIENT_SECRET=generate-strong-secret-min-32-chars
JWT_PRIVATE_KEY_PATH=classpath:key/private_key.pem
JWT_PUBLIC_KEY_PATH=classpath:key/public_key.pem
JWT_ISSUER=logistic
JWT_AUDIENCE=logistic-api
SECURITY_PASSWORD=bcrypt_encoded_password
```

### Mail Configuration (SENSITIVE)
```bash
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=noreply@yourdomain.com
MAIL_PASSWORD=app_specific_password     # ✅ Use app passwords, not account password
```

### CORS Configuration (SECURITY)
```bash
# Production: comma-separated list of allowed origins
CORS_ALLOWED_ORIGINS=https://yourdomain.com,https://app.yourdomain.com

# Development
CORS_ALLOWED_ORIGINS=http://localhost:3000,http://localhost:4200
```

### API Configuration
```bash
API_AUTH_URL=https://api.yourdomain.com/auth
```

### Service Discovery (Eureka)
```bash
EUREKA_HOSTNAME=service-auth-1
EUREKA_URL=http://eureka-server:9000/eureka/
```

### Configuration Server
```bash
CONFIG_SERVER_URL=http://config-server:8071
```

### Monitoring
```bash
PROMETHEUS_ENABLED=true
```

### Payment Gateway (ABA)
```bash
ABA_MERCHANT_ID=merchant_id_from_aba
ABA_API_KEY=api_key_from_aba        # ✅ SENSITIVE
ABA_API_URL=https://checkout.payway.com.kh/api/payment-gateway/v1/payments/generate-qr
ABA_CALLBACK_URL=https://yourdomain.com/callback
```

### Telegram Notifications (SENSITIVE)
```bash
TELEGRAM_CHAT_ID=your_chat_id
TELEGRAM_API_TOKEN=your_bot_token   # ✅ SENSITIVE
```

### SSL/TLS Configuration (Production)
```bash
SSL_KEYSTORE_PATH=/etc/ssl/keystore.p12
SSL_KEYSTORE_PASSWORD=keystore_password
```

### Logging
```bash
LOG_LEVEL=INFO              # Can be: DEBUG, INFO, WARN, ERROR
LOG_PATH=/var/log/service-auth
```

---

## Environment-Specific Examples

### Development Environment (.env or docker-compose)
```bash
# .env file for development
SPRING_PROFILES_ACTIVE=dev
DB_URL=jdbc:mysql://localhost:3308/student_register
DB_USERNAME=root
DB_PASSWORD=
JWT_CLIENT_SECRET=dev-secret-only-for-testing
CORS_ALLOWED_ORIGINS=http://localhost:3000,http://localhost:4200
MAIL_USERNAME=dev-email@gmail.com
MAIL_PASSWORD=dev-app-password
EUREKA_URL=http://localhost:9000/eureka/
```

### Staging Environment
```bash
SPRING_PROFILES_ACTIVE=staging
DB_URL=jdbc:mysql://staging-db:3306/student_register
DB_USERNAME=${STAGING_DB_USER}
DB_PASSWORD=${STAGING_DB_PASSWORD}
JWT_CLIENT_SECRET=${STAGING_JWT_SECRET}
CORS_ALLOWED_ORIGINS=https://staging.yourdomain.com
MAIL_USERNAME=staging@yourdomain.com
MAIL_PASSWORD=${STAGING_MAIL_PASSWORD}
EUREKA_URL=http://eureka-staging:9000/eureka/
CONFIG_SERVER_URL=http://config-server-staging:8071
```

### Production Environment
```bash
SPRING_PROFILES_ACTIVE=prod
DB_URL=jdbc:mysql://prod-db-primary:3306/student_register?useSSL=true&serverTimezone=UTC
DB_USERNAME=${PROD_DB_USER}
DB_PASSWORD=${PROD_DB_PASSWORD}
JWT_CLIENT_SECRET=${PROD_JWT_SECRET}
CORS_ALLOWED_ORIGINS=https://yourdomain.com,https://api.yourdomain.com
MAIL_USERNAME=noreply@yourdomain.com
MAIL_PASSWORD=${PROD_MAIL_PASSWORD}
EUREKA_URL=http://eureka-prod-1:9000/eureka/,http://eureka-prod-2:9000/eureka/
CONFIG_SERVER_URL=http://config-server:8071
SSL_KEYSTORE_PATH=/etc/ssl/certs/keystore.p12
SSL_KEYSTORE_PASSWORD=${PROD_SSL_PASSWORD}
ABA_MERCHANT_ID=${PROD_ABA_MERCHANT_ID}
ABA_API_KEY=${PROD_ABA_API_KEY}
```

---

## Kubernetes Secrets Example

```yaml
apiVersion: v1
kind: Secret
metadata:
  name: service-auth-secrets
  namespace: production
type: Opaque
stringData:
  DB_URL: "jdbc:mysql://mysql-service:3306/student_register?useSSL=true"
  DB_USERNAME: "sa_user"
  DB_PASSWORD: "CHANGE_ME_IN_VAULT"
  JWT_CLIENT_SECRET: "GENERATE_STRONG_SECRET_32_CHARS_MIN"
  MAIL_PASSWORD: "GMAIL_APP_PASSWORD"
  ABA_API_KEY: "CHANGE_ME"
  TELEGRAM_API_TOKEN: "CHANGE_ME"
  SSL_KEYSTORE_PASSWORD: "CHANGE_ME"
```

---

## Docker Compose Example

```yaml
version: '3.9'

services:
  service-auth:
    build:
      context: .
      dockerfile: Dockerfile.prod
    ports:
      - "4800:4800"
    environment:
      SPRING_PROFILES_ACTIVE: prod
      DB_URL: "jdbc:mysql://mysql:3306/student_register"
      DB_USERNAME: "${DB_USERNAME}"
      DB_PASSWORD: "${DB_PASSWORD}"
      JWT_CLIENT_SECRET: "${JWT_CLIENT_SECRET}"
      MAIL_PASSWORD: "${MAIL_PASSWORD}"
      EUREKA_URL: "http://eureka:9000/eureka/"
    depends_on:
      mysql:
        condition: service_healthy
    networks:
      - microservices
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:4800/auth/actuator/health"]
      interval: 30s
      timeout: 5s
      retries: 3
      start_period: 30s

  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: "${DB_PASSWORD}"
      MYSQL_DATABASE: "student_register"
    volumes:
      - mysql_data:/var/lib/mysql
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "localhost"]
      interval: 10s
      timeout: 5s
      retries: 5

volumes:
  mysql_data:

networks:
  microservices:
    driver: bridge
```

---

## Vault Integration (HashiCorp Vault Example)

```hcl
# Save secrets to Vault
vault kv put secret/service-auth/prod \
  db_password="your_secure_password" \
  jwt_client_secret="your_jwt_secret" \
  mail_password="your_mail_password" \
  aba_api_key="your_aba_key"
```

---

## Security Best Practices

✅ **DO:**
- Store all credentials in environment variables
- Use HashiCorp Vault for secret management
- Rotate credentials regularly
- Use AWS Secrets Manager or Azure Key Vault in cloud
- Enable audit logging for secret access
- Use unique passwords per environment

❌ **DON'T:**
- Hardcode credentials in properties files
- Store passwords in git repositories
- Use same credentials across environments
- Share credentials via email
- Log sensitive data

---

## Local Development Setup

```bash
# Create .env.local file
cat > .env.local << EOF
SPRING_PROFILES_ACTIVE=dev
DB_URL=jdbc:mysql://localhost:3308/student_register
DB_USERNAME=root
DB_PASSWORD=
JWT_CLIENT_SECRET=dev-secret-change-in-production
CORS_ALLOWED_ORIGINS=http://localhost:3000,http://localhost:4200
EOF

# Load environment variables
set -a
source .env.local
set +a

# Run with Maven
mvn spring-boot:run
```

---

## Verification Checklist

- [ ] All required environment variables are set
- [ ] No credentials are hardcoded in files
- [ ] Database connection is successful
- [ ] JWT keys are properly loaded
- [ ] Mail configuration works
- [ ] Eureka registration successful
- [ ] Health endpoint accessible
- [ ] Swagger UI available (dev only)
- [ ] CORS headers correct
- [ ] Logging configured appropriately

