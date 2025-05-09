# Task 5: Reset Processor API

## Overview

This task involves implementing the API endpoint for resetting all registers to zero. You will create a new endpoint that executes the RST instruction, which sets all registers (A, B, C, D) back to their initial state of zero.

## Prerequisites

- Completion of Task 1: Core Domain Model Implementation
- Completion of Task 2: Execute Single Instruction API
- Completion of Task 3: Execute Batch Instructions API
- Completion of Task 4: Register Value APIs
- Understanding of RESTful API design

## Objectives

1. Implement the service method for resetting the processor
2. Create the API endpoint for resetting the processor
3. Write unit and integration tests

## Detailed Steps

### 1. Implement Service Method for Resetting the Processor

Add a new method to the ProcessorService class:

```java
public Map<String, Integer> resetProcessor() {
    processor.executeInstruction("RST");
    return processor.getAllRegisterValues();
}
```

### 2. Implement Controller Endpoint for Resetting the Processor

Add a new endpoint to the ProcessorController class:

```java
@PostMapping("/processor/reset")
public ResponseEntity<ApiResponse> resetProcessor() {
    Map<String, Integer> registers = processorService.resetProcessor();
    return ResponseEntity.ok(ApiResponse.success(registers));
}
```

### 3. Write Unit Tests for Reset Processor Endpoint

Create unit tests for the reset processor endpoint:

#### ProcessorServiceTest

```java
@Test
public void testResetProcessor() {
    // Arrange
    Map<String, Integer> resetRegisters = new HashMap<>();
    resetRegisters.put("A", 0);
    resetRegisters.put("B", 0);
    resetRegisters.put("C", 0);
    resetRegisters.put("D", 0);
    
    when(mockProcessor.getAllRegisterValues()).thenReturn(resetRegisters);
    
    // Act
    Map<String, Integer> result = processorService.resetProcessor();
    
    // Assert
    assertEquals(resetRegisters, result);
    verify(mockProcessor).executeInstruction("RST");
    verify(mockProcessor).getAllRegisterValues();
}
```

#### ProcessorControllerTest

```java
@Test
public void testResetProcessor() throws Exception {
    // Arrange
    Map<String, Integer> resetRegisters = new HashMap<>();
    resetRegisters.put("A", 0);
    resetRegisters.put("B", 0);
    resetRegisters.put("C", 0);
    resetRegisters.put("D", 0);
    
    when(processorService.resetProcessor()).thenReturn(resetRegisters);
    
    // Act & Assert
    mockMvc.perform(post("/api/v1/processor/reset"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("success"))
            .andExpect(jsonPath("$.registers.A").value(0))
            .andExpect(jsonPath("$.registers.B").value(0))
            .andExpect(jsonPath("$.registers.C").value(0))
            .andExpect(jsonPath("$.registers.D").value(0));
    
    verify(processorService).resetProcessor();
}
```

### 4. Write Integration Test for Reset Processor Endpoint

Create an integration test for the reset processor endpoint:

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ResetProcessorIntegrationTest {
    
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
    public void testResetProcessor() {
        // Verify registers are not zero
        ResponseEntity<Map> getResponse = restTemplate.getForEntity("/api/v1/registers", Map.class);
        Map<String, Integer> registers = getResponse.getBody();
        assertEquals(42, registers.get("A"));
        
        // Reset processor
        ResponseEntity<Map> resetResponse = restTemplate.postForEntity("/api/v1/processor/reset", null, Map.class);
        
        assertEquals(200, resetResponse.getStatusCodeValue());
        Map<String, Object> body = resetResponse.getBody();
        assertEquals("success", body.get("status"));
        
        Map<String, Integer> resetRegisters = (Map<String, Integer>) body.get("registers");
        assertEquals(0, resetRegisters.get("A"));
        assertEquals(0, resetRegisters.get("B"));
        assertEquals(0, resetRegisters.get("C"));
        assertEquals(0, resetRegisters.get("D"));
        
        // Verify registers are reset
        ResponseEntity<Map> verifyResponse = restTemplate.getForEntity("/api/v1/registers", Map.class);
        Map<String, Integer> verifyRegisters = verifyResponse.getBody();
        assertEquals(0, verifyRegisters.get("A"));
        assertEquals(0, verifyRegisters.get("B"));
        assertEquals(0, verifyRegisters.get("C"));
        assertEquals(0, verifyRegisters.get("D"));
    }
}
```

## Expected Outcome

After completing this task, you will have:

1. A service method for resetting the processor
2. A REST API endpoint for resetting the processor (POST /api/v1/processor/reset)
3. Comprehensive unit and integration tests

## Extensibility Considerations

The reset processor API is designed to be extensible:
- The endpoint can be extended to support selective reset of specific registers
- The response format can be enhanced to include additional information
- The reset operation can be optimized if needed

## Next Steps

After completing this task, proceed to Task 6: API Documentation and Final Integration, where you will implement Swagger/OpenAPI documentation and create end-to-end tests that use multiple endpoints.
