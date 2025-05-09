# Technical Context: Opcode Microprocessor Simulator

## Technologies Used

### Core Technologies

1. **Java**
   - Version: Java 17 (or later)
   - Rationale: Strong typing system helps prevent errors when dealing with register operations, excellent unit testing frameworks, object-oriented features support SOLID principles and extensibility, good performance for computational operations.

2. **Spring Boot**
   - Version: Spring Boot 3.x
   - Rationale: Provides a robust framework for building RESTful APIs, includes comprehensive testing support, offers dependency injection for clean architecture, and simplifies configuration.

3. **Spring Web**
   - Purpose: Provides the foundation for building web applications and RESTful services
   - Features: RESTful controllers, request mapping, response handling

4. **Spring Test**
   - Purpose: Supports testing of Spring components
   - Features: MockMvc for controller testing, Spring Boot Test for integration testing

5. **JUnit 5**
   - Purpose: Testing framework for Java applications
   - Features: Assertions, test lifecycle management, parameterized tests

6. **Lombok**
   - Purpose: Reduces boilerplate code
   - Features: Automatic generation of getters, setters, constructors, builders, etc.

7. **Springdoc OpenAPI**
   - Purpose: API documentation
   - Features: Automatic generation of OpenAPI documentation, Swagger UI integration

### Build Tools

1. **Gradle**
   - Purpose: Dependency management and build automation
   - Features: Declarative dependency management, build lifecycle, task automation

### Development Tools

1. **Git**
   - Purpose: Version control
   - Features: Branching, merging, history tracking

2. **IDE Support**
   - Recommended: IntelliJ IDEA, Eclipse, or VS Code with Java extensions
   - Features: Code completion, refactoring, debugging

## Development Setup

### Prerequisites

1. **Java Development Kit (JDK)**
   - Version: 17 or later
   - Installation: Via package manager or direct download

2. **Gradle**
   - Version: 7.x or later
   - Installation: Via package manager or direct download

### Project Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd opcode-sample
   ```

2. **Build the project**
   ```bash
   ./gradlew build
   ```

3. **Run the application**
   ```bash
   ./gradlew bootRun
   ```

4. **Run tests**
   ```bash
   ./gradlew test
   ```

### API Access

Once the application is running:

1. **API Endpoints**
   - Base URL: `http://localhost:8080/api/v1`
   - Available endpoints:
     - POST `/instructions` - Execute a single instruction
     - POST `/instructions/batch` - Execute multiple instructions
     - GET `/registers` - Get all register values
     - GET `/registers/{register}` - Get specific register value
     - POST `/processor/reset` - Reset processor state

2. **Swagger UI**
   - URL: `http://localhost:8080/swagger-ui.html`
   - Features: Interactive API documentation and testing

## Technical Constraints

1. **Register Limitations**
   - Type: 32-bit signed integers
   - Range: -2,147,483,648 to 2,147,483,647
   - Overflow/Underflow: Standard Java integer overflow/underflow behavior

2. **Instruction Set Constraints**
   - Limited to the 7 defined instructions (SET, ADR, ADD, MOV, INR, DCR, RST)
   - No support for conditional operations or jumps
   - No memory operations beyond registers

3. **Performance Considerations**
   - Single-threaded execution model
   - No persistent storage of processor state between application restarts

## Dependencies

### Core Dependencies

```gradle
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springdoc:springdoc-openapi-ui:1.6.9'
    compileOnly 'org.projectlombok:lombok'
    annotationProcessor 'org.projectlombok:lombok'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
}
```

### Key Third-Party Libraries

1. **springdoc-openapi-ui**
   - Purpose: API documentation
   - Version: 1.6.9 or compatible with Spring Boot 3.x

2. **lombok**
   - Purpose: Boilerplate code reduction
   - Version: Compatible with Java 17 and Spring Boot 3.x

## Tool Usage Patterns

### API Testing

1. **Curl Commands**
   ```bash
   # Execute a single instruction
   curl -X POST http://localhost:8080/api/v1/instructions \
     -H "Content-Type: application/json" \
     -d '{"instruction": "SET A 42"}'
   
   # Execute multiple instructions
   curl -X POST http://localhost:8080/api/v1/instructions/batch \
     -H "Content-Type: application/json" \
     -d '{"instructions": ["SET A 10", "SET B 20", "ADR A B"]}'
   
   # Get all register values
   curl -X GET http://localhost:8080/api/v1/registers
   
   # Get specific register value
   curl -X GET http://localhost:8080/api/v1/registers/A
   
   # Reset processor
   curl -X POST http://localhost:8080/api/v1/processor/reset
   ```

2. **Swagger UI**
   - Interactive API documentation and testing through the Swagger UI interface

### Development Workflow

1. **TDD Approach**
   - Write tests first
   - Implement functionality
   - Refactor as needed

2. **Code Organization**
   - Package by feature
   - Clear separation of concerns
   - Follow Spring Boot conventions

3. **Version Control**
   - Feature branches
   - Pull requests for code review
   - Merge to main branch after approval

## Deployment Considerations

1. **Containerization**
   - Docker support for containerized deployment
   - Docker Compose for local development

2. **Environment Configuration**
   - Spring profiles for different environments (dev, test, prod)
   - Externalized configuration through application.properties/yml

3. **Monitoring**
   - Spring Boot Actuator for health checks and metrics
   - Logging with SLF4J and Logback
