# 🧪 API Testing Sequence Guide

This document provides a comprehensive sequence of curl commands to test the Opcode Microprocessor Simulator REST APIs. The commands are organized to demonstrate a complete workflow from resetting the processor to executing various instructions and checking register values.

## 🎯 Test Sequence Overview

1. Reset processor state
2. Verify register initialization
3. Test single instruction execution
4. Test register value retrieval
5. Test arithmetic operations
6. Test batch instruction execution
7. Test error handling
8. Verify final state

## 📝 Test Commands

### 1️⃣ Reset Processor
Reset the processor to ensure a clean state:
```bash
curl -X POST http://localhost:8080/api/v1/processor/reset \
  -H "Content-Type: application/json" \
  -v
```

### 2️⃣ Check Initial State
Verify all registers are initialized to zero:
```bash
curl -X GET http://localhost:8080/api/v1/registers \
  -H "Accept: application/json" \
  -v
```

### 3️⃣ Set Register Value
Execute a single SET instruction to set register A to 42:
```bash
curl -X POST http://localhost:8080/api/v1/instructions \
  -H "Content-Type: application/json" \
  -d '{"instruction": "SET A 42"}' \
  -v
```

### 4️⃣ Verify Register Value
Check that register A contains 42:
```bash
curl -X GET http://localhost:8080/api/v1/registers/A \
  -H "Accept: application/json" \
  -v
```

### 5️⃣ Set Second Register
Set register B to 10:
```bash
curl -X POST http://localhost:8080/api/v1/instructions \
  -H "Content-Type: application/json" \
  -d '{"instruction": "SET B 10"}' \
  -v
```

### 6️⃣ Test Addition
Add registers A and B using ADR instruction (A = A + B):
```bash
curl -X POST http://localhost:8080/api/v1/instructions \
  -H "Content-Type: application/json" \
  -d '{"instruction": "ADR A B"}' \
  -v
```

### 7️⃣ Verify Addition Result
Check that register A contains 52 (42 + 10):
```bash
curl -X GET http://localhost:8080/api/v1/registers/A \
  -H "Accept: application/json" \
  -v
```

### 8️⃣ Test Batch Instructions
Execute multiple operations in a single request:
```bash
curl -X POST http://localhost:8080/api/v1/instructions/batch \
  -H "Content-Type: application/json" \
  -d '{
    "instructions": [
      "SET C 15",
      "MOV D C",
      "INR D",
      "DCR C"
    ]
  }' \
  -v
```

### 9️⃣ Verify Batch Results
Check all registers to verify the batch execution:
```bash
curl -X GET http://localhost:8080/api/v1/registers \
  -H "Accept: application/json" \
  -v
```

### 🔟 Test Invalid Instruction
Test error handling with an invalid instruction:
```bash
curl -X POST http://localhost:8080/api/v1/instructions \
  -H "Content-Type: application/json" \
  -d '{"instruction": "INVALID A B"}' \
  -v
```

### 1️⃣1️⃣ Test Invalid Register
Test error handling with an invalid register:
```bash
curl -X POST http://localhost:8080/api/v1/instructions \
  -H "Content-Type: application/json" \
  -d '{"instruction": "SET X 10"}' \
  -v
```

### 1️⃣2️⃣ Final Reset
Reset the processor to clean state:
```bash
curl -X POST http://localhost:8080/api/v1/processor/reset \
  -H "Content-Type: application/json" \
  -v
```

### 1️⃣3️⃣ Verify Final State
Confirm all registers are back to zero:
```bash
curl -X GET http://localhost:8080/api/v1/registers \
  -H "Accept: application/json" \
  -v
```

## 📋 Expected Results

After running the complete test sequence:

1. Initial state: All registers = 0
2. After SET A 42: A = 42, others = 0
3. After SET B 10: A = 42, B = 10, others = 0
4. After ADR A B: A = 52, B = 10, others = 0
5. After batch operations:
   - A = 52
   - B = 10
   - C = 14 (15 - 1 from DCR)
   - D = 16 (15 + 1 from INR)
6. Final state: All registers = 0

## 🔍 Troubleshooting Tips

1. Use the `-v` flag to see detailed request/response information
2. Check HTTP status codes in responses:
   - 200: Successful operation
   - 400: Invalid instruction or register
   - 500: Internal server error
3. Verify Content-Type headers are set correctly
4. Ensure JSON payload is properly formatted

## 🚀 Running the Tests

You can:
1. Copy and paste commands individually
2. Save commands in a shell script
3. Use a tool like Postman to import and run the requests

For automated testing, consider converting these curl commands to integration tests using tools like REST Assured or Spring's TestRestTemplate.
