# Task 1: Core Domain Model Implementation

## Overview

This task involves implementing the foundational components of the Opcode Microprocessor Simulator. You will create the register manager that maintains the state of the microprocessor's registers and the basic exception handling mechanism. You will also define the instruction interface and abstract base class that all specific instructions will extend.

## Objectives

1. Implement the RegisterManager class
2. Create custom exception classes
3. Define the Instruction interface and AbstractInstruction class
4. Write unit tests for all components

## Detailed Steps

### 1. Create Exception Classes

Create the following exception classes in the `com.opcode.exception` package:

```java
// Base exception class
public abstract class OpcodeException extends RuntimeException {
    public OpcodeException(String message) {
        super(message);
    }
}

// For invalid register names
public class InvalidRegisterException extends OpcodeException {
    public InvalidRegisterException(String message) {
        super(message);
    }
}

// For invalid instruction types
public class InvalidInstructionException extends OpcodeException {
    public InvalidInstructionException(String message) {
        super(message);
    }
}

// For invalid syntax
public class InvalidSyntaxException extends OpcodeException {
    public InvalidSyntaxException(String message) {
        super(message);
    }
}
```

### 2. Implement RegisterManager

Create the RegisterManager class in the `com.opcode.core` package:

```java
import com.opcode.exception.InvalidRegisterException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RegisterManager {
    private final Map<String, Integer> registers;
    
    public RegisterManager() {
        registers = new HashMap<>();
        // Initialize registers A, B, C, D with value 0
        registers.put("A", 0);
        registers.put("B", 0);
        registers.put("C", 0);
        registers.put("D", 0);
    }
    
    public Integer getValue(String register) {
        validateRegister(register);
        return registers.get(register);
    }
    
    public void setValue(String register, Integer value) {
        validateRegister(register);
        registers.put(register, value);
    }
    
    public void reset() {
        registers.replaceAll((k, v) -> 0);
    }
    
    public Map<String, Integer> getAllRegisters() {
        return Collections.unmodifiableMap(registers);
    }
    
    public boolean isValidRegister(String register) {
        return registers.containsKey(register);
    }
    
    private void validateRegister(String register) {
        if (!isValidRegister(register)) {
            throw new InvalidRegisterException("Invalid register: " + register);
        }
    }
}
```

### 3. Define Instruction Interface and Abstract Class

Create the Instruction interface and AbstractInstruction class in the `com.opcode.instruction` package:

```java
// Instruction interface
public interface Instruction {
    void execute(RegisterManager registerManager);
    boolean validate();
}

// Abstract base class for common functionality
public abstract class AbstractInstruction implements Instruction {
    protected final String[] args;
    
    public AbstractInstruction(String[] args) {
        this.args = args;
    }
    
    @Override
    public abstract void execute(RegisterManager registerManager);
    
    @Override
    public abstract boolean validate();
    
    protected void validateRegister(String register, RegisterManager registerManager) {
        if (!registerManager.isValidRegister(register)) {
            throw new InvalidRegisterException("Invalid register: " + register);
        }
    }
}
```

### 4. Write Unit Tests

Create unit tests for the RegisterManager and exception classes:

```java
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RegisterManagerTest {
    private RegisterManager registerManager;
    
    @BeforeEach
    public void setUp() {
        registerManager = new RegisterManager();
    }
    
    @Test
    public void testInitialization() {
        assertEquals(0, registerManager.getValue("A"));
        assertEquals(0, registerManager.getValue("B"));
        assertEquals(0, registerManager.getValue("C"));
        assertEquals(0, registerManager.getValue("D"));
    }
    
    @Test
    public void testSetValue() {
        registerManager.setValue("A", 42);
        assertEquals(42, registerManager.getValue("A"));
    }
    
    @Test
    public void testReset() {
        registerManager.setValue("A", 42);
        registerManager.setValue("B", 24);
        registerManager.reset();
        assertEquals(0, registerManager.getValue("A"));
        assertEquals(0, registerManager.getValue("B"));
    }
    
    @Test
    public void testInvalidRegister() {
        assertThrows(InvalidRegisterException.class, () -> {
            registerManager.getValue("X");
        });
        
        assertThrows(InvalidRegisterException.class, () -> {
            registerManager.setValue("X", 10);
        });
    }
    
    @Test
    public void testIsValidRegister() {
        assertTrue(registerManager.isValidRegister("A"));
        assertTrue(registerManager.isValidRegister("B"));
        assertTrue(registerManager.isValidRegister("C"));
        assertTrue(registerManager.isValidRegister("D"));
        assertFalse(registerManager.isValidRegister("X"));
    }
    
    @Test
    public void testGetAllRegisters() {
        Map<String, Integer> registers = registerManager.getAllRegisters();
        assertEquals(4, registers.size());
        assertTrue(registers.containsKey("A"));
        assertTrue(registers.containsKey("B"));
        assertTrue(registers.containsKey("C"));
        assertTrue(registers.containsKey("D"));
    }
}
```

## Expected Outcome

After completing this task, you will have:

1. A fully functional RegisterManager that can:
   - Store and retrieve register values
   - Reset all registers to zero
   - Validate register names

2. A set of custom exceptions for error handling

3. An Instruction interface and AbstractInstruction class that define the contract for all instruction implementations

4. Comprehensive unit tests for all components

## Extensibility Considerations

The RegisterManager is designed to be easily extensible:
- New registers can be added by simply adding new entries to the registers map in the constructor
- No changes to the core logic are needed to support additional registers

## Next Steps

After completing this task, proceed to Task 2: Execute Single Instruction API, where you will implement the specific instruction classes and the API endpoint for executing a single instruction.
