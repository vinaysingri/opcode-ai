# API Specification: Opcode Microprocessor Simulator

## Document Information
- **Version**: 1.0
- **Date**: September 5, 2025
- **Status**: Draft

## 1. Overview

This document specifies a minimal REST API for the Opcode Microprocessor Simulator. The API provides endpoints to execute instructions, retrieve register values, and manage the processor state.

## 2. Base URL

```
/api/v1
```

## 3. Endpoints

### 3.1 Execute Instruction

Executes a single instruction on the microprocessor.

- **URL**: `/instructions`
- **Method**: `POST`
- **Request Body**:
  ```json
  {
    "instruction": "string"
  }
  ```
  Where `instruction` is a valid instruction string (e.g., "SET A 10", "ADR C D", etc.)

- **Response**:
  - **Status Code**: 200 OK
  - **Body**:
    ```json
    {
      "status": "success",
      "registers": {
        "A": 0,
        "B": 0,
        "C": 0,
        "D": 0
      }
    }
    ```

- **Error Responses**:
  - **Status Code**: 400 Bad Request
    ```json
    {
      "status": "error",
      "message": "Invalid instruction syntax"
    }
    ```
  - **Status Code**: 400 Bad Request
    ```json
    {
      "status": "error",
      "message": "Invalid register: X"
    }
    ```

### 3.2 Execute Multiple Instructions

Executes a sequence of instructions on the microprocessor.

- **URL**: `/instructions/batch`
- **Method**: `POST`
- **Request Body**:
  ```json
  {
    "instructions": [
      "SET A 10",
      "SET B 20",
      "ADR A B"
    ]
  }
  ```

- **Response**:
  - **Status Code**: 200 OK
  - **Body**:
    ```json
    {
      "status": "success",
      "registers": {
        "A": 30,
        "B": 20,
        "C": 0,
        "D": 0
      }
    }
    ```

- **Error Responses**:
  - **Status Code**: 400 Bad Request
    ```json
    {
      "status": "error",
      "message": "Invalid instruction syntax at index 1: SET X 10",
      "executedInstructions": 1
    }
    ```

### 3.3 Get Register Values

Retrieves the current values of all registers.

- **URL**: `/registers`
- **Method**: `GET`
- **Response**:
  - **Status Code**: 200 OK
  - **Body**:
    ```json
    {
      "A": 0,
      "B": 0,
      "C": 0,
      "D": 0
    }
    ```

### 3.4 Get Specific Register Value

Retrieves the current value of a specific register.

- **URL**: `/registers/{register}`
- **Method**: `GET`
- **URL Parameters**:
  - `register`: The name of the register (A, B, C, or D)
- **Response**:
  - **Status Code**: 200 OK
  - **Body**:
    ```json
    {
      "value": 0
    }
    ```

- **Error Responses**:
  - **Status Code**: 404 Not Found
    ```json
    {
      "status": "error",
      "message": "Invalid register: X"
    }
    ```

### 3.5 Reset Processor

Resets all registers to zero.

- **URL**: `/processor/reset`
- **Method**: `POST`
- **Response**:
  - **Status Code**: 200 OK
  - **Body**:
    ```json
    {
      "status": "success",
      "registers": {
        "A": 0,
        "B": 0,
        "C": 0,
        "D": 0
      }
    }
    ```

## 4. Error Handling

All endpoints return appropriate HTTP status codes:

- **200 OK**: Request successful
- **400 Bad Request**: Invalid input (e.g., invalid instruction syntax)
- **404 Not Found**: Resource not found (e.g., invalid register)
- **500 Internal Server Error**: Server-side error

Error responses include a JSON body with:
- `status`: Always "error" for error responses
- `message`: Human-readable error message
- Additional fields as appropriate for the specific error

## 5. Examples

### Example 1: Setting a register value

**Request**:
```http
POST /api/v1/instructions
Content-Type: application/json

{
  "instruction": "SET A 42"
}
```

**Response**:
```http
HTTP/1.1 200 OK
Content-Type: application/json

{
  "status": "success",
  "registers": {
    "A": 42,
    "B": 0,
    "C": 0,
    "D": 0
  }
}
```

### Example 2: Executing a sequence of instructions

**Request**:
```http
POST /api/v1/instructions/batch
Content-Type: application/json

{
  "instructions": [
    "SET A 10",
    "SET B 20",
    "ADR A B",
    "INR A"
  ]
}
```

**Response**:
```http
HTTP/1.1 200 OK
Content-Type: application/json

{
  "status": "success",
  "registers": {
    "A": 31,
    "B": 20,
    "C": 0,
    "D": 0
  }
}
```

## 6. Implementation Notes

### 6.1 Controller Implementation

The API can be implemented using Spring Boot with a controller structure like:

```java
@RestController
@RequestMapping("/api/v1")
public class ProcessorController {
    
    private final Processor processor;
    
    public ProcessorController(Processor processor) {
        this.processor = processor;
    }
    
    @PostMapping("/instructions")
    public ResponseEntity<?> executeInstruction(@RequestBody InstructionRequest request) {
        // Implementation
    }
    
    @PostMapping("/instructions/batch")
    public ResponseEntity<?> executeBatchInstructions(@RequestBody BatchInstructionRequest request) {
        // Implementation
    }
    
    @GetMapping("/registers")
    public ResponseEntity<?> getAllRegisters() {
        // Implementation
    }
    
    @GetMapping("/registers/{register}")
    public ResponseEntity<?> getRegisterValue(@PathVariable String register) {
        // Implementation
    }
    
    @PostMapping("/processor/reset")
    public ResponseEntity<?> resetProcessor() {
        // Implementation
    }
}
```

### 6.2 Exception Handling

Implement a global exception handler using `@ControllerAdvice` to handle custom exceptions:

```java
@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(InvalidRegisterException.class)
    public ResponseEntity<?> handleInvalidRegisterException(InvalidRegisterException ex) {
        // Implementation
    }
    
    @ExceptionHandler(InvalidInstructionException.class)
    public ResponseEntity<?> handleInvalidInstructionException(InvalidInstructionException ex) {
        // Implementation
    }
    
    @ExceptionHandler(InvalidSyntaxException.class)
    public ResponseEntity<?> handleInvalidSyntaxException(InvalidSyntaxException ex) {
        // Implementation
    }
}
```

### 6.3 Service Layer Integration

The controller will delegate to the `Processor` class from the core implementation:

```java
@Service
public class ProcessorService {
    
    private final Processor processor;
    
    public ProcessorService() {
        RegisterManager registerManager = new RegisterManager();
        InstructionFactory factory = new InstructionFactory();
        InstructionParser parser = new InstructionParser(factory);
        this.processor = new Processor(registerManager, parser);
    }
    
    public Map<String, Integer> executeInstruction(String instructionText) {
        processor.executeInstruction(instructionText);
        return processor.getAllRegisterValues();
    }
    
    public Map<String, Integer> executeBatchInstructions(List<String> instructions) {
        for (String instruction : instructions) {
            processor.executeInstruction(instruction);
        }
        return processor.getAllRegisterValues();
    }
    
    public Map<String, Integer> getAllRegisters() {
        return processor.getAllRegisterValues();
    }
    
    public Integer getRegisterValue(String register) {
        return processor.getRegisterValue(register);
    }
    
    public Map<String, Integer> resetProcessor() {
        processor.executeInstruction("RST");
        return processor.getAllRegisterValues();
    }
}
```

## 7. Conclusion

This API specification provides a minimal but complete interface to the Opcode Microprocessor Simulator. It exposes all the required functionality while maintaining a clean, RESTful design. The API is designed to be easily extensible for future enhancements such as new instructions or registers.
