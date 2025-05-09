# Task 2: Execute Single Instruction API

## Overview

This task involves implementing the instruction classes for the seven instruction types specified in the requirements (SET, ADR, ADD, MOV, INR, DCR, RST), creating the instruction factory and parser, and implementing the API endpoint for executing a single instruction.

## Prerequisites

- Completion of Task 1: Core Domain Model Implementation
- Understanding of the Command pattern and Factory pattern

## Objectives

1. Implement all seven instruction classes
2. Create the instruction factory
3. Implement the instruction parser
4. Create the processor facade
5. Implement the service method for executing a single instruction
6. Create the API endpoint for executing a single instruction
7. Write unit and integration tests

## Detailed Steps

### 1. Implement Instruction Classes

Create the following instruction classes in the `com.opcode.instruction` package:

#### SetInstruction

```java
import com.opcode.core.RegisterManager;
import com.opcode.exception.InvalidSyntaxException;

public class SetInstruction extends AbstractInstruction {
    public SetInstruction(String[] args) {
        super(args);
    }
    
    @Override
    public void execute(RegisterManager registerManager) {
        if (!validate()) {
            throw new InvalidSyntaxException("Invalid SET instruction syntax");
        }
        
        String register = args[0];
        int value = Integer.parseInt(args[1]);
        
        registerManager.setValue(register, value);
    }
    
    @Override
    public boolean validate() {
        return args.length == 2 && args[0] != null && args[1] != null;
    }
}
```

#### AdrInstruction

```java
import com.opcode.core.RegisterManager;
import com.opcode.exception.InvalidSyntaxException;

public class AdrInstruction extends AbstractInstruction {
    public AdrInstruction(String[] args) {
        super(args);
    }
    
    @Override
    public void execute(RegisterManager registerManager) {
        if (!validate()) {
            throw new InvalidSyntaxException("Invalid ADR instruction syntax");
        }
        
        String targetRegister = args[0];
        String sourceRegister = args[1];
        
        int targetValue = registerManager.getValue(targetRegister);
        int sourceValue = registerManager.getValue(sourceRegister);
        
        registerManager.setValue(targetRegister, targetValue + sourceValue);
    }
    
    @Override
    public boolean validate() {
        return args.length == 2 && args[0] != null && args[1] != null;
    }
}
```

#### AddInstruction

```java
import com.opcode.core.RegisterManager;
import com.opcode.exception.InvalidSyntaxException;

public class AddInstruction extends AbstractInstruction {
    public AddInstruction(String[] args) {
        super(args);
    }
    
    @Override
    public void execute(RegisterManager registerManager) {
        if (!validate()) {
            throw new InvalidSyntaxException("Invalid ADD instruction syntax");
        }
        
        String register = args[0];
        int value = Integer.parseInt(args[1]);
        
        int currentValue = registerManager.getValue(register);
        registerManager.setValue(register, currentValue + value);
    }
    
    @Override
    public boolean validate() {
        return args.length == 2 && args[0] != null && args[1] != null;
    }
}
```

#### MovInstruction

```java
import com.opcode.core.RegisterManager;
import com.opcode.exception.InvalidSyntaxException;

public class MovInstruction extends AbstractInstruction {
    public MovInstruction(String[] args) {
        super(args);
    }
    
    @Override
    public void execute(RegisterManager registerManager) {
        if (!validate()) {
            throw new InvalidSyntaxException("Invalid MOV instruction syntax");
        }
        
        String targetRegister = args[0];
        String sourceRegister = args[1];
        
        int sourceValue = registerManager.getValue(sourceRegister);
        registerManager.setValue(targetRegister, sourceValue);
    }
    
    @Override
    public boolean validate() {
        return args.length == 2 && args[0] != null && args[1] != null;
    }
}
```

#### InrInstruction

```java
import com.opcode.core.RegisterManager;
import com.opcode.exception.InvalidSyntaxException;

public class InrInstruction extends AbstractInstruction {
    public InrInstruction(String[] args) {
        super(args);
    }
    
    @Override
    public void execute(RegisterManager registerManager) {
        if (!validate()) {
            throw new InvalidSyntaxException("Invalid INR instruction syntax");
        }
        
        String register = args[0];
        int currentValue = registerManager.getValue(register);
        registerManager.setValue(register, currentValue + 1);
    }
    
    @Override
    public boolean validate() {
        return args.length == 1 && args[0] != null;
    }
}
```

#### DcrInstruction

```java
import com.opcode.core.RegisterManager;
import com.opcode.exception.InvalidSyntaxException;

public class DcrInstruction extends AbstractInstruction {
    public DcrInstruction(String[] args) {
        super(args);
    }
    
    @Override
    public void execute(RegisterManager registerManager) {
        if (!validate()) {
            throw new InvalidSyntaxException("Invalid DCR instruction syntax");
        }
        
        String register = args[0];
        int currentValue = registerManager.getValue(register);
        registerManager.setValue(register, currentValue - 1);
    }
    
    @Override
    public boolean validate() {
        return args.length == 1 && args[0] != null;
    }
}
```

#### RstInstruction

```java
import com.opcode.core.RegisterManager;
import com.opcode.exception.InvalidSyntaxException;

public class RstInstruction extends AbstractInstruction {
    public RstInstruction(String[] args) {
        super(args);
    }
    
    @Override
    public void execute(RegisterManager registerManager) {
        if (!validate()) {
            throw new InvalidSyntaxException("Invalid RST instruction syntax");
        }
        
        registerManager.reset();
    }
    
    @Override
    public boolean validate() {
        return args.length == 0;
    }
}
```

### 2. Create Instruction Factory

Create the InstructionFactory class in the `com.opcode.instruction` package:

```java
import com.opcode.exception.InvalidInstructionException;

public class InstructionFactory {
    public Instruction createInstruction(String type, String[] args) {
        switch (type.toUpperCase()) {
            case "SET":
                return new SetInstruction(args);
            case "ADR":
                return new AdrInstruction(args);
            case "ADD":
                return new AddInstruction(args);
            case "MOV":
                return new MovInstruction(args);
            case "INR":
                return new InrInstruction(args);
            case "DCR":
                return new DcrInstruction(args);
            case "RST":
                return new RstInstruction(args);
            default:
                throw new InvalidInstructionException("Unknown instruction type: " + type);
        }
    }
}
```

### 3. Implement Instruction Parser

Create the InstructionParser class in the `com.opcode.parser` package:

```java
import com.opcode.exception.InvalidSyntaxException;
import com.opcode.instruction.Instruction;
import com.opcode.instruction.InstructionFactory;
import java.util.Arrays;

public class InstructionParser {
    private final InstructionFactory factory;
    
    public InstructionParser(InstructionFactory factory) {
        this.factory = factory;
    }
    
    public Instruction parse(String instructionText) {
        if (!validateSyntax(instructionText)) {
            throw new InvalidSyntaxException("Invalid instruction syntax: " + instructionText);
        }
        
        String[] parts = instructionText.trim().split("\\s+");
        String instructionType = parts[0];
        String[] args = Arrays.copyOfRange(parts, 1, parts.length);
        
        return factory.createInstruction(instructionType, args);
    }
    
    private boolean validateSyntax(String instructionText) {
        // Basic syntax validation
        if (instructionText == null || instructionText.trim().isEmpty()) {
            return false;
        }
        
        String[] parts = instructionText.trim().split("\\s+");
        if (parts.length == 0) {
            return false;
        }
        
        // Further validation can be added based on instruction type
        return true;
    }
}
```

### 4. Create Processor Facade

Create the Processor class in the `com.opcode.core` package:

```java
import com.opcode.instruction.Instruction;
import com.opcode.parser.InstructionParser;
import java.util.Map;

public class Processor {
    private final RegisterManager registerManager;
    private final InstructionParser parser;
    
    public Processor(RegisterManager registerManager, InstructionParser parser) {
        this.registerManager = registerManager;
        this.parser = parser;
    }
    
    public void executeInstruction(String instructionText) {
        Instruction instruction = parser.parse(instructionText);
        instruction.execute(registerManager);
    }
    
    public Integer getRegisterValue(String register) {
        return registerManager.getValue(register);
    }
    
    public Map<String, Integer> getAllRegisterValues() {
        return registerManager.getAllRegisters();
    }
}
```

### 5. Implement Service Layer

Create the ProcessorService class in the `com.opcode.service` package:

```java
import com.opcode.core.Processor;
import com.opcode.core.RegisterManager;
import com.opcode.instruction.InstructionFactory;
import com.opcode.parser.InstructionParser;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class ProcessorService {
    
    private final Processor processor;
    
    public ProcessorService() {
        RegisterManager registerManager = new RegisterManager();
        InstructionFactory factory = new InstructionFactory();
        InstructionParser parser = new InstructionParser(factory);
        this.processor = new Processor(registerManager, parser);
    }
    
    // Constructor for testing with mock dependencies
    ProcessorService(Processor processor) {
        this.processor = processor;
    }
    
    public Map<String, Integer> executeInstruction(String instructionText) {
        processor.executeInstruction(instructionText);
        return processor.getAllRegisterValues();
    }
    
    public Map<String, Integer> getAllRegisters() {
        return processor.getAllRegisterValues();
    }
    
    public Integer getRegisterValue(String register) {
        return processor.getRegisterValue(register);
    }
}
```

### 6. Create Request and Response Models

Create the following model classes in the `com.opcode.model` package:

#### InstructionRequest

```java
public class InstructionRequest {
    private String instruction;
    
    // Getters, setters, constructors
    
    public String getInstruction() {
        return instruction;
    }
    
    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }
    
    // Default constructor for JSON deserialization
    public InstructionRequest() {
    }
    
    public InstructionRequest(String instruction) {
        this.instruction = instruction;
    }
}
```

#### ApiResponse

```java
import java.util.Map;

public class ApiResponse {
    private String status;
    private String message;
    private Map<String, Integer> registers;
    
    // Getters, setters, constructors
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public Map<String, Integer> getRegisters() {
        return registers;
    }
    
    public void setRegisters(Map<String, Integer> registers) {
        this.registers = registers;
    }
    
    // Static factory methods for common responses
    
    public static ApiResponse success(Map<String, Integer> registers) {
        ApiResponse response = new ApiResponse();
        response.setStatus("success");
        response.setRegisters(registers);
        return response;
    }
    
    public static ApiResponse error(String message) {
        ApiResponse response = new ApiResponse();
        response.setStatus("error");
        response.setMessage(message);
        return response;
    }
}
```

### 7. Implement REST Controller

Create the ProcessorController class in the `com.opcode.controller` package:

```java
import com.opcode.model.ApiResponse;
import com.opcode.model.InstructionRequest;
import com.opcode.service.ProcessorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class ProcessorController {
    
    private final ProcessorService processorService;
    
    public ProcessorController(ProcessorService processorService) {
        this.processorService = processorService;
    }
    
    @PostMapping("/instructions")
    public ResponseEntity<ApiResponse> executeInstruction(@RequestBody InstructionRequest request) {
        Map<String, Integer> registers = processorService.executeInstruction(request.getInstruction());
        return ResponseEntity.ok(ApiResponse.success(registers));
    }
}
```

### 8. Set Up Global Exception Handling

Create a global exception handler in the `com.opcode.exception` package:

```java
import com.opcode.model.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(InvalidRegisterException.class)
    public ResponseEntity<ApiResponse> handleInvalidRegisterException(InvalidRegisterException ex) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ApiResponse.error(ex.getMessage()));
    }
    
    @ExceptionHandler(InvalidInstructionException.class)
    public ResponseEntity<ApiResponse> handleInvalidInstructionException(InvalidInstructionException ex) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.error(ex.getMessage()));
    }
    
    @ExceptionHandler(InvalidSyntaxException.class)
    public ResponseEntity<ApiResponse> handleInvalidSyntaxException(InvalidSyntaxException ex) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.error(ex.getMessage()));
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGenericException(Exception ex) {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponse.error("An unexpected error occurred: " + ex.getMessage()));
    }
}
```

### 9. Write Unit Tests

Create unit tests for each instruction type, the factory, parser, processor, service, and controller. Here are examples for a few key components:

#### InstructionFactoryTest

```java
import com.opcode.exception.InvalidInstructionException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class InstructionFactoryTest {
    private InstructionFactory factory = new InstructionFactory();
    
    @Test
    public void testCreateSetInstruction() {
        Instruction instruction = factory.createInstruction("SET", new String[]{"A", "10"});
        assertTrue(instruction instanceof SetInstruction);
    }
    
    @Test
    public void testCreateAdrInstruction() {
        Instruction instruction = factory.createInstruction("ADR", new String[]{"A", "B"});
        assertTrue(instruction instanceof AdrInstruction);
    }
    
    @Test
    public void testInvalidInstructionType() {
        assertThrows(InvalidInstructionException.class, () -> {
            factory.createInstruction("INVALID", new String[]{});
        });
    }
}
```

#### ProcessorControllerTest

```java
import com.opcode.model.ApiResponse;
import com.opcode.model.InstructionRequest;
import com.opcode.service.ProcessorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProcessorController.class)
public class ProcessorControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private ProcessorService processorService;
    
    private Map<String, Integer> registers;
    
    @BeforeEach
    public void setUp() {
        registers = new HashMap<>();
        registers.put("A", 10);
        registers.put("B", 20);
        registers.put("C", 0);
        registers.put("D", 0);
    }
    
    @Test
    public void testExecuteInstruction() throws Exception {
        when(processorService.executeInstruction("SET A 10")).thenReturn(registers);
        
        mockMvc.perform(post("/api/v1/instructions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"instruction\":\"SET A 10\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.registers.A").value(10))
                .andExpect(jsonPath("$.registers.B").value(20));
        
        verify(processorService).executeInstruction("SET A 10");
    }
}
```

### 10. Integration Test

Create an integration test that tests the entire flow from controller to core components:

```java
import com.opcode.model.InstructionRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ExecuteInstructionIntegrationTest {
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Test
    public void testExecuteInstruction() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        InstructionRequest request = new InstructionRequest("SET A 42");
        HttpEntity<InstructionRequest> entity = new HttpEntity<>(request, headers);
        
        ResponseEntity<Map> response = restTemplate.postForEntity("/api/v1/instructions", entity, Map.class);
        
        assertEquals(200, response.getStatusCodeValue());
        Map<String, Object> body = response.getBody();
        assertEquals("success", body.get("status"));
        
        Map<String, Integer> registers = (Map<String, Integer>) body.get("registers");
        assertEquals(42, registers.get("A"));
    }
}
```

## Expected Outcome

After completing this task, you will have:

1. Seven instruction classes that implement the Instruction interface
2. An InstructionFactory that creates the appropriate instruction object
3. An InstructionParser that parses instruction text
4. A Processor facade that coordinates execution
5. A service layer that connects to the core components
6. A REST API endpoint for executing a single instruction
7. Comprehensive unit and integration tests

## Extensibility Considerations

The implementation is designed to be extensible:
- New instructions can be added by creating new classes and updating the factory
- The parser can be extended to handle more complex syntax
- The API can be extended with additional endpoints

## Next Steps

After completing this task, proceed to Task 3: Execute Batch Instructions API, where you will implement the API endpoint for executing multiple instructions in a batch.
