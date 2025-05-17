I understand your frustration. Here's the complete README.md file as a single, continuous block with no breaks between sections:

````markdown
# 🏦 DTB Banking Platform Technical Test

A modular banking platform enabling customers to access card services through three core microservices.

## 📊 Data Flow Diagrams

### Level 0 DFD (Context Diagram)

```mermaid
flowchart TD
    CUSTOMER([Customer]) <--> PLATFORM[[Banking Platform]]
    PLATFORM <--> EXTERNAL[[External Bank Systems]]

    style CUSTOMER fill:#ffd6e7,stroke:#333
    style PLATFORM fill:#d6f5ff,stroke:#333,stroke-width:2px
    style EXTERNAL fill:#e3ffd6,stroke:#333
```
````

### Level 1 DFD (Microservices Breakdown)

```mermaid
flowchart LR
    subgraph MICROSERVICES["Microservices (Platform)"]
        direction TB
        CUSTOMER_SERVICE[Customer Service\n:8081] --> CUSTOMER_DB[(Customer DB)]
        ACCOUNT_SERVICE[Account Service\n:8082] --> ACCOUNT_DB[(Account DB)]
        CARD_SERVICE[Card Service\n:8083] --> CARD_DB[(Card DB)]
    end

    CUSTOMER_SERVICE <-..-> ACCOUNT_SERVICE
    ACCOUNT_SERVICE <-..-> CARD_SERVICE

    style MICROSERVICES fill:#f0f0f0,stroke:#555,stroke-width:2px
    style CUSTOMER_SERVICE fill:#b3e6ff,stroke:#333
    style ACCOUNT_SERVICE fill:#b3e6ff,stroke:#333
    style CARD_SERVICE fill:#b3e6ff,stroke:#333
    style CUSTOMER_DB fill:#ffcc99,stroke:#333
    style ACCOUNT_DB fill:#ffcc99,stroke:#333
    style CARD_DB fill:#ffcc99,stroke:#333
```

## 📦 Services

| Service  | Port | Swagger UI                                    | Description                          |
| -------- | ---- | --------------------------------------------- | ------------------------------------ |
| Customer | 8081 | http://localhost:8081/swagger-ui/index.html#/ | Manages customer profiles and data   |
| Account  | 8082 | http://localhost:8082/swagger-ui/index.html#/ | Handles accounts and transactions    |
| Card     | 8083 | http://localhost:8083/swagger-ui/index.html#/ | Manages card issuance and operations |

## 🚀 Getting Started

### Prerequisites

- Java 17+
- Maven 3.8+
- Docker (optional)

### Installation

## `Build and Run Project`

> Requires **JDK 21** to run the project, **Git** to pull from remote repository, and **Docker** to run docker-compose

Follow the steps to build and run the project:

- Clone the repository from Git

```bash
  git clone git clone https://github.com/AmosMbeki/DTBBankingPlatformTechnicalTest.git
```

- Open the project file

```shell
  cd DTBBankingPlatformTechnicalTest
```

- Run the project with Docker

```shell
  docker-compose up -d && docker-compose down
```

<!-- ## 🧪 Testing
```bash
mvn test
``` -->

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Open a Pull Request

```

```
