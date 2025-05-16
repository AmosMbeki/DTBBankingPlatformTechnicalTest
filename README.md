# 🏦 DTB Banking Platform Technical Test

A modular banking platform enabling customers to access card services through three core microservices.

## 📦 Services

| Service       | Port  | Swagger UI                                        | Description                          |
|---------------|-------|---------------------------------------------------|--------------------------------------|
| Customer      | 8081  | http://localhost:8081/swagger-ui/index.html#/     | Manages customer profiles and data   |
| Account       | 8082  | http://localhost:8082/swagger-ui/index.html#/     | Handles accounts and transactions    |
| Card          | 8083  | http://localhost:8083/swagger-ui/index.html#/     | Manages card issuance and operations |

## 🚀 Getting Started

### Prerequisites
- Java 17+
- Maven 3.8+
- Docker

### Installation
```bash
git clone https://github.com/AmosMbeki/DTBBankingPlatformTechnicalTest.git
cd DTBBankingPlatformTechnicalTest
mvn clean install