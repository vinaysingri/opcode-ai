# Task 6: API Documentation and Final Integration

## Overview

This task involves implementing Swagger/OpenAPI documentation for the Opcode Microprocessor Simulator API and creating end-to-end tests that use multiple endpoints. You will also ensure that the implementation is extensible for future enhancements.

## Prerequisites

- Completion of Task 1: Core Domain Model Implementation
- Completion of Task 2: Execute Single Instruction API
- Completion of Task 3: Execute Batch Instructions API
- Completion of Task 4: Register Value APIs
- Completion of Task 5: Reset Processor API
- Understanding of Swagger/OpenAPI documentation

## Objectives

1. Implement Swagger/OpenAPI documentation
2. Create end-to-end tests that use multiple endpoints
3. Ensure extensibility for future instructions and registers
4. Perform final validation and testing

## Detailed Steps

### 1. Add Swagger/OpenAPI Dependencies

Add the following dependencies to your `build.gradle` or `pom.xml` file:

For Gradle:
```groovy
implementation 'org.springdoc:springdoc-openapi-ui:1.6.9'
implementation 'org.springdoc:springdoc-openapi-webmvc-core:1.6.9'
```

For Maven:
```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-ui</artifactId>
    <version>1.6.9</version>
</dependency>
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-webmvc-core</artifactId>
    <version>1.6.9</version>
</dependency>
```

### 2. Configure OpenAPI Documentation

Create a configuration class for OpenAPI documentation:

```java
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI opcodeOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Opcode Microprocessor Simulator API")
                        .description("API for simulating a custom microprocessor with a specific instruction set")
                        .version("v1.0.0")
                        .license(new License().name("MIT").url("https://opensource.org/licenses/MIT")));
    }
}
```

### 3. Add API Documentation Annotations

Add OpenAPI annotations to the controller classes:

```java
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Processor", description = "Opcode Microprocessor Simulator API")
public class ProcessorController {
    
    // ... existing code ...
    
    @Operation(summary = "Execute a single instruction", description = "Executes a single instruction on the microprocessor")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Instruction executed successfully",
                content = @Content(schema = @Schema(implementation = ApiResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid instruction syntax or type",
                content = @Content(schema = @Schema(implementation = ApiResponse.class))),
        @ApiResponse(responseCode = "404", description = "Invalid register",
                content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PostMapping("/instructions")
    public ResponseEntity<com.opcode.model.ApiResponse> executeInstruction(@RequestBody InstructionRequest request) {
        // ... existing code ...
    }
    
    // Add similar annotations to other endpoints
}
```

### 4. Configure Application Properties

Add the following properties to `application.properties`:

```properties
# OpenAPI Documentation
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.operationsSorter=method
```

### 5. Create End-to-End Tests

Create end-to-end tests that use multiple endpoints:

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EndToEndIntegrationTest {
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Test
    public void testCompleteWorkflow() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        // Step 1: Reset processor
        restTemplate.postForEntity("/api/v1/processor/reset", null, Map.class);
        
        // Step 2: Verify all registers are zero
        ResponseEntity<Map> getResponse = restTemplate.getForEntity("/api/v1/registers", Map.class);
        Map<String, Integer> registers = getResponse.getBody();
        assertEquals(0, registers.get("A"));
        assertEquals(0, registers.get("B"));
        assertEquals(0, registers.get("C"));
        assertEquals(0, registers.get("D"));
        
        // Step 3: Execute a single instruction
        InstructionRequest setRequest = new InstructionRequest("SET A 42");
        HttpEntity<InstructionRequest> setEntity = new HttpEntity<>(setRequest, headers);
        restTemplate.postForEntity("/api/v1/instructions", setEntity, Map.class);
        
        // Step 4: Verify register A is updated
        ResponseEntity<Map> getAResponse = restTemplate.getForEntity("/api/v1/registers/A", Map.class);
        Map<String, Integer> aValue = getAResponse.getBody();
        assertEquals(42, aValue.get("value"));
        
        // Step 5: Execute batch instructions
        List<String> instructions = Arrays.asList("SET B 10", "ADR A B");
        BatchInstructionRequest batchRequest = new BatchInstructionRequest(instructions);
        HttpEntity<BatchInstructionRequest> batchEntity = new HttpEntity<>(batchRequest, headers);
        restTemplate.postForEntity("/api/v1/instructions/batch", batchEntity, Map.class);
        
        // Step 6: Verify registers are updated
        ResponseEntity<Map> finalResponse = restTemplate.getForEntity("/api/v1/registers", Map.class);
        Map<String, Integer> finalRegisters = finalResponse.getBody();
        assertEquals(52, finalRegisters.get("A"));  // 42 + 10
        assertEquals(10, finalRegisters.get("B"));
        assertEquals(0, finalRegisters.get("C"));
        assertEquals(0, finalRegisters.get("D"));
    }
}
```

### 6. Ensure Extensibility for Future Instructions

Document how to add new instructions to the system:

```java
/**
 * Steps to add a new instruction:
 * 
 * 1. Create a new class that extends AbstractInstruction
 * 2. Implement the execute and validate methods
 * 3. Update the InstructionFactory to create the new instruction type
 * 
 * Example for a new XOR instruction:
 */
public class XorInstruction extends AbstractInstruction {
    public XorInstruction(String[] args) {
        super(args);
    }
    
    @Override
    public void execute(RegisterManager registerManager) {
        if (!validate()) {
            throw new InvalidSyntaxException("Invalid XOR instruction syntax");
        }
        
        String targetRegister = args[0];
        String sourceRegister = args[1];
        
        int targetValue = registerManager.getValue(targetRegister);
        int sourceValue = registerManager.getValue(sourceRegister);
        
        registerManager.setValue(targetRegister, targetValue ^ sourceValue);
    }
    
    @Override
    public boolean validate() {
        return args.length == 2 && args[0] != null && args[1] != null;
    }
}

// Then update the InstructionFactory:
public Instruction createInstruction(String type, String[] args) {
    switch (type.toUpperCase()) {
        // ... existing cases ...
        case "XOR":
            return new XorInstruction(args);
        default:
            throw new InvalidInstructionException("Unknown instruction type: " + type);
    }
}
```

### 7. Ensure Extensibility for Future Registers

Document how to add new registers to the system:

```java
/**
 * Steps to add new registers:
 * 
 * 1. Update the RegisterManager constructor to initialize the new registers
 * 
 * Example for adding registers E and F:
 */
public RegisterManager() {
    registers = new HashMap<>();
    // Initialize registers A, B, C, D, E, F with value 0
    registers.put("A", 0);
    registers.put("B", 0);
    registers.put("C", 0);
    registers.put("D", 0);
    registers.put("E", 0);
    registers.put("F", 0);
}
```

### 8. Perform Final Validation and Testing

Create a test that validates all API endpoints:

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApiValidationTest {
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Test
    public void validateAllEndpoints() {
        // Test execute instruction endpoint
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        InstructionRequest request = new InstructionRequest("SET A 42");
        HttpEntity<InstructionRequest> entity = new HttpEntity<>(request, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity("/api/v1/instructions", entity, Map.class);
        assertEquals(200, response.getStatusCodeValue());
        
        // Test batch execution endpoint
        List<String> instructions = Arrays.asList("SET B 10", "ADR A B");
        BatchInstructionRequest batchRequest = new BatchInstructionRequest(instructions);
        HttpEntity<BatchInstructionRequest> batchEntity = new HttpEntity<>(batchRequest, headers);
        ResponseEntity<Map> batchResponse = restTemplate.postForEntity("/api/v1/instructions/batch", batchEntity, Map.class);
        assertEquals(200, batchResponse.getStatusCodeValue());
        
        // Test get all registers endpoint
        ResponseEntity<Map> getAllResponse = restTemplate.getForEntity("/api/v1/registers", Map.class);
        assertEquals(200, getAllResponse.getStatusCodeValue());
        
        // Test get specific register endpoint
        ResponseEntity<Map> getAResponse = restTemplate.getForEntity("/api/v1/registers/A", Map.class);
        assertEquals(200, getAResponse.getStatusCodeValue());
        
        // Test reset processor endpoint
        ResponseEntity<Map> resetResponse = restTemplate.postForEntity("/api/v1/processor/reset", null, Map.class);
        assertEquals(200, resetResponse.getStatusCodeValue());
    }
}
```

## Expected Outcome

After completing this task, you will have:

1. Comprehensive API documentation using Swagger/OpenAPI
2. End-to-end tests that validate the complete workflow
3. Documentation on how to extend the system with new instructions and registers
4. Final validation of all API endpoints

## Extensibility Considerations

The implementation is designed to be extensible:
- New instructions can be added by creating new command classes and updating the factory
- New registers can be added by updating the RegisterManager
- The API documentation can be enhanced with more detailed information
- Additional endpoints can be added to support new features

## Conclusion

This completes the implementation of the Opcode Microprocessor Simulator. The system now provides a complete set of API endpoints for executing instructions, retrieving register values, and resetting the processor. The implementation is well-documented, thoroughly tested, and designed for extensibility.
