# Task 4: Register Value APIs

## Overview

This task involves implementing the API endpoints for retrieving register values. You will create two endpoints: one for retrieving all register values and another for retrieving a specific register value. You will also implement proper error handling for invalid registers.

## Prerequisites

- Completion of Task 1: Core Domain Model Implementation
- Completion of Task 2: Execute Single Instruction API
- Completion of Task 3: Execute Batch Instructions API
- Understanding of RESTful API design

## Objectives

1. Create the response model for register values
2. Implement the service methods for retrieving register values
3. Create the API endpoints for retrieving register values
4. Implement error handling for invalid registers
5. Write unit and integration tests

## Detailed Steps

### 1. Create Register Value Response Model

Create the RegisterValueResponse class in the `com.opcode.model` package:

```java
public class RegisterValueResponse {
    private int value;
    
    // Getters, setters, constructors
    
    public int getValue() {
        return value;
    }
    
    public void setValue(int value) {
        this.value = value;
    }
    
    // Default constructor for JSON serialization
    public RegisterValueResponse() {
    }
    
    public RegisterValueResponse(int value) {
        this.value = value;
    }
}
```

### 2. Implement Controller Endpoints for Register Values

Add new endpoints to the ProcessorController class:

```java
@GetMapping("/registers")
public ResponseEntity<Map<String, Integer>> getAllRegisters() {
    return ResponseEntity.ok(processorService.getAllRegisters());
}

@GetMapping("/registers/{register}")
public ResponseEntity<RegisterValueResponse> getRegisterValue(@PathVariable String register) {
    Integer value = processorService.getRegisterValue(register);
    return ResponseEntity.ok(new RegisterValueResponse(value));
}
```

### 3. Write Unit Tests for Register Value Endpoints

Create unit tests for the register value endpoints:

#### ProcessorControllerTest

```java
@Test
public void testGetAllRegisters() throws Exception {
    // Arrange
    when(processorService.getAllRegisters()).thenReturn(registers);
    
    // Act & Assert
    mockMvc.perform(get("/api/v1/registers"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.A").value(10))
            .andExpect(jsonPath("$.B").value(20))
            .andExpect(jsonPath("$.C").value(0))
            .andExpect(jsonPath("$.D").value(0));
    
    verify(processorService).getAllRegisters();
}

@Test
public void testGetRegisterValue() throws Exception {
    // Arrange
    when(processorService.getRegisterValue("A")).thenReturn(10);
    
    // Act & Assert
    mockMvc.perform(get("/api/v1/registers/A"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.value").value(10));
    
    verify(processorService).getRegisterValue("A");
}

@Test
public void testGetRegisterValue_InvalidRegister() throws Exception {
    // Arrange
    when(processorService.getRegisterValue("X"))
        .thenThrow(new InvalidRegisterException("Invalid register: X"));
    
    // Act & Assert
    mockMvc.perform(get("/api/v1/registers/X"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value("error"))
            .andExpect(jsonPath("$.message").value("Invalid register: X"));
    
    verify(processorService).getRegisterValue("X");
}
```

### 4. Write Integration Tests for Register Value Endpoints

Create integration tests for the register value endpoints:

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RegisterValueIntegrationTest {
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @BeforeEach
    public void setUp() {
        // Set up some register values
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        InstructionRequest request = new InstructionRequest("SET A 42");
        HttpEntity<InstructionRequest> entity = new HttpEntity<>(request, headers);
        
        restTemplate.postForEntity("/api/v1/instructions", entity, Map.class);
    }
    
    @Test
    public void testGetAllRegisters() {
        ResponseEntity<Map> response = restTemplate.getForEntity("/api/v1/registers", Map.class);
        
        assertEquals(200, response.getStatusCodeValue());
        Map<String, Integer> registers = response.getBody();
        assertEquals(42, registers.get("A"));
    }
    
    @Test
    public void testGetRegisterValue() {
        ResponseEntity<Map> response = restTemplate.getForEntity("/api/v1/registers/A", Map.class);
        
        assertEquals(200, response.getStatusCodeValue());
        Map<String, Integer> body = response.getBody();
        assertEquals(42, body.get("value"));
    }
    
    @Test
    public void testGetRegisterValue_InvalidRegister() {
        ResponseEntity<Map> response = restTemplate.getForEntity("/api/v1/registers/X", Map.class);
        
        assertEquals(404, response.getStatusCodeValue());
        Map<String, Object> body = response.getBody();
        assertEquals("error", body.get("status"));
        assertTrue(((String) body.get("message")).contains("Invalid register: X"));
    }
}
```

## Expected Outcome

After completing this task, you will have:

1. A RegisterValueResponse model for returning register values
2. Two API endpoints:
   - GET /api/v1/registers - Returns all register values
   - GET /api/v1/registers/{register} - Returns a specific register value
3. Proper error handling for invalid registers
4. Comprehensive unit and integration tests

## Extensibility Considerations

The register value APIs are designed to be extensible:
- The endpoints can be extended to support additional query parameters
- The response format can be enhanced to include additional information
- The error handling can be improved to provide more detailed error messages

## Next Steps

After completing this task, proceed to Task 5: Reset Processor API, where you will implement the API endpoint for resetting all registers to zero.
