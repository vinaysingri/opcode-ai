# Task 3: Execute Batch Instructions API

## Overview

This task involves implementing the API endpoint for executing multiple instructions in a batch. Building on the foundation established in Task 2, you will create a new endpoint that accepts a list of instructions and executes them sequentially. You will also implement error handling for partial execution.

## Prerequisites

- Completion of Task 1: Core Domain Model Implementation
- Completion of Task 2: Execute Single Instruction API
- Understanding of batch processing and error handling

## Objectives

1. Create the request model for batch instructions
2. Implement the service method for executing multiple instructions
3. Create the API endpoint for batch execution
4. Implement error handling for partial execution
5. Write unit and integration tests

## Detailed Steps

### 1. Create Batch Instruction Request Model

Create the BatchInstructionRequest class in the `com.opcode.model` package:

```java
import java.util.List;

public class BatchInstructionRequest {
    private List<String> instructions;
    
    // Getters, setters, constructors
    
    public List<String> getInstructions() {
        return instructions;
    }
    
    public void setInstructions(List<String> instructions) {
        this.instructions = instructions;
    }
    
    // Default constructor for JSON deserialization
    public BatchInstructionRequest() {
    }
    
    public BatchInstructionRequest(List<String> instructions) {
        this.instructions = instructions;
    }
}
```

### 2. Update ApiResponse for Batch Execution

Update the ApiResponse class to include information about partially executed instructions:

```java
public class ApiResponse {
    private String status;
    private String message;
    private Map<String, Integer> registers;
    private Integer executedInstructions;
    
    // Add getter and setter for executedInstructions
    
    public Integer getExecutedInstructions() {
        return executedInstructions;
    }
    
    public void setExecutedInstructions(Integer executedInstructions) {
        this.executedInstructions = executedInstructions;
    }
    
    // Add a factory method for batch error responses
    
    public static ApiResponse batchError(String message, int executedInstructions) {
        ApiResponse response = error(message);
        response.setExecutedInstructions(executedInstructions);
        return response;
    }
}
```

### 3. Create BatchExecutionException

Create a new exception class for batch execution errors:

```java
public class BatchExecutionException extends OpcodeException {
    private final int executedInstructions;
    
    public BatchExecutionException(String message, int executedInstructions) {
        super(message);
        this.executedInstructions = executedInstructions;
    }
    
    public int getExecutedInstructions() {
        return executedInstructions;
    }
}
```

### 4. Implement Service Method for Batch Execution

Add a new method to the ProcessorService class:

```java
public Map<String, Integer> executeBatchInstructions(List<String> instructions) {
    for (int i = 0; i < instructions.size(); i++) {
        try {
            processor.executeInstruction(instructions.get(i));
        } catch (Exception e) {
            throw new BatchExecutionException(
                "Error executing instruction at index " + i + ": " + instructions.get(i) + " - " + e.getMessage(),
                i
            );
        }
    }
    return processor.getAllRegisterValues();
}
```

### 5. Update Global Exception Handler

Add a handler for BatchExecutionException in the GlobalExceptionHandler class:

```java
@ExceptionHandler(BatchExecutionException.class)
public ResponseEntity<ApiResponse> handleBatchExecutionException(BatchExecutionException ex) {
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(ApiResponse.batchError(ex.getMessage(), ex.getExecutedInstructions()));
}
```

### 6. Implement Batch Execution Controller Endpoint

Add a new endpoint to the ProcessorController class:

```java
@PostMapping("/instructions/batch")
public ResponseEntity<ApiResponse> executeBatchInstructions(@RequestBody BatchInstructionRequest request) {
    Map<String, Integer> registers = processorService.executeBatchInstructions(request.getInstructions());
    return ResponseEntity.ok(ApiResponse.success(registers));
}
```

### 7. Write Unit Tests for Batch Execution

Create unit tests for the batch execution functionality:

#### ProcessorServiceTest

```java
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProcessorServiceTest {
    
    @Mock
    private Processor mockProcessor;
    
    private ProcessorService processorService;
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        processorService = new ProcessorService(mockProcessor);
    }
    
    @Test
    public void testExecuteBatchInstructions() {
        // Arrange
        List<String> instructions = Arrays.asList("SET A 10", "SET B 20", "ADR A B");
        Map<String, Integer> registers = new HashMap<>();
        registers.put("A", 30);
        registers.put("B", 20);
        
        when(mockProcessor.getAllRegisterValues()).thenReturn(registers);
        
        // Act
        Map<String, Integer> result = processorService.executeBatchInstructions(instructions);
        
        // Assert
        assertEquals(registers, result);
        verify(mockProcessor).executeInstruction("SET A 10");
        verify(mockProcessor).executeInstruction("SET B 20");
        verify(mockProcessor).executeInstruction("ADR A B");
        verify(mockProcessor).getAllRegisterValues();
    }
    
    @Test
    public void testExecuteBatchInstructions_Error() {
        // Arrange
        List<String> instructions = Arrays.asList("SET A 10", "INVALID B 20", "ADR A B");
        
        doThrow(new InvalidInstructionException("Unknown instruction type: INVALID"))
            .when(mockProcessor).executeInstruction("INVALID B 20");
        
        // Act & Assert
        BatchExecutionException exception = assertThrows(BatchExecutionException.class, () -> {
            processorService.executeBatchInstructions(instructions);
        });
        
        assertEquals(1, exception.getExecutedInstructions());
        verify(mockProcessor).executeInstruction("SET A 10");
        verify(mockProcessor).executeInstruction("INVALID B 20");
        verify(mockProcessor, never()).executeInstruction("ADR A B");
    }
}
```

#### ProcessorControllerTest

```java
@Test
public void testExecuteBatchInstructions() throws Exception {
    // Arrange
    List<String> instructions = Arrays.asList("SET A 10", "SET B 20");
    when(processorService.executeBatchInstructions(instructions)).thenReturn(registers);
    
    // Act & Assert
    mockMvc.perform(post("/api/v1/instructions/batch")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"instructions\":[\"SET A 10\",\"SET B 20\"]}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("success"))
            .andExpect(jsonPath("$.registers.A").value(10))
            .andExpect(jsonPath("$.registers.B").value(20));
    
    verify(processorService).executeBatchInstructions(instructions);
}

@Test
public void testExecuteBatchInstructions_Error() throws Exception {
    // Arrange
    List<String> instructions = Arrays.asList("SET A 10", "INVALID B 20");
    when(processorService.executeBatchInstructions(instructions))
        .thenThrow(new BatchExecutionException("Error executing instruction at index 1", 1));
    
    // Act & Assert
    mockMvc.perform(post("/api/v1/instructions/batch")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"instructions\":[\"SET A 10\",\"INVALID B 20\"]}"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value("error"))
            .andExpect(jsonPath("$.executedInstructions").value(1));
    
    verify(processorService).executeBatchInstructions(instructions);
}
```

### 8. Write Integration Tests for Batch Execution

Create an integration test for the batch execution endpoint:

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BatchExecutionIntegrationTest {
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Test
    public void testBatchExecution() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        List<String> instructions = Arrays.asList("SET A 10", "SET B 20", "ADR A B");
        BatchInstructionRequest request = new BatchInstructionRequest(instructions);
        HttpEntity<BatchInstructionRequest> entity = new HttpEntity<>(request, headers);
        
        ResponseEntity<Map> response = restTemplate.postForEntity("/api/v1/instructions/batch", entity, Map.class);
        
        assertEquals(200, response.getStatusCodeValue());
        Map<String, Object> body = response.getBody();
        assertEquals("success", body.get("status"));
        
        Map<String, Integer> registers = (Map<String, Integer>) body.get("registers");
        assertEquals(30, registers.get("A"));
        assertEquals(20, registers.get("B"));
    }
    
    @Test
    public void testBatchExecution_Error() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        List<String> instructions = Arrays.asList("SET A 10", "INVALID B 20");
        BatchInstructionRequest request = new BatchInstructionRequest(instructions);
        HttpEntity<BatchInstructionRequest> entity = new HttpEntity<>(request, headers);
        
        ResponseEntity<Map> response = restTemplate.postForEntity("/api/v1/instructions/batch", entity, Map.class);
        
        assertEquals(400, response.getStatusCodeValue());
        Map<String, Object> body = response.getBody();
        assertEquals("error", body.get("status"));
        assertEquals(1, body.get("executedInstructions"));
    }
}
```

## Expected Outcome

After completing this task, you will have:

1. A BatchInstructionRequest model for batch execution
2. An updated ApiResponse model that includes information about partially executed instructions
3. A service method for executing multiple instructions
4. A REST API endpoint for batch execution
5. Error handling for partial execution
6. Comprehensive unit and integration tests

## Extensibility Considerations

The batch execution implementation is designed to be extensible:
- The error handling mechanism can be extended to provide more detailed information about failures
- The batch execution can be optimized for performance if needed
- Additional batch operations can be added in the future

## Next Steps

After completing this task, proceed to Task 4: Register Value APIs, where you will implement the API endpoints for retrieving register values.
