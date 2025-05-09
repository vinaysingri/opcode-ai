package com.opcode.service;

import com.opcode.core.Processor;
import com.opcode.exception.BatchExecutionException;
import com.opcode.exception.InvalidInstructionException;
import com.opcode.exception.InvalidRegisterException;
import com.opcode.exception.InvalidSyntaxException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the ProcessorService class.
 */
@ExtendWith(MockitoExtension.class)
public class ProcessorServiceTest {
    
    @Mock
    private Processor processor;
    
    private ProcessorService service;
    
    @BeforeEach
    void setUp() {
        service = new ProcessorService(processor);
    }
    
    @Test
    void testExecuteInstruction() {
        // Arrange
        String instruction = "SET A 42";
        Map<String, Integer> expectedRegisters = new HashMap<>();
        expectedRegisters.put("A", 42);
        expectedRegisters.put("B", 0);
        expectedRegisters.put("C", 0);
        expectedRegisters.put("D", 0);
        
        doNothing().when(processor).executeInstruction(instruction);
        when(processor.getAllRegisterValues()).thenReturn(expectedRegisters);
        
        // Act
        Map<String, Integer> result = service.executeInstruction(instruction);
        
        // Assert
        assertAll(
            () -> verify(processor).executeInstruction(instruction),
            () -> verify(processor).getAllRegisterValues(),
            () -> assertEquals(expectedRegisters, result)
        );
    }
    
    @Test
    void testExecuteInstructionWithInvalidSyntax() {
        // Arrange
        String instruction = "INVALID";
        doThrow(new InvalidSyntaxException("Invalid syntax"))
            .when(processor).executeInstruction(instruction);
        
        // Act & Assert
        assertThrows(InvalidSyntaxException.class, () -> {
            service.executeInstruction(instruction);
        });
    }
    
    @Test
    void testExecuteInstructionWithInvalidRegister() {
        // Arrange
        String instruction = "SET X 10";
        doThrow(new InvalidRegisterException("Invalid register"))
            .when(processor).executeInstruction(instruction);
        
        // Act & Assert
        assertThrows(InvalidRegisterException.class, () -> {
            service.executeInstruction(instruction);
        });
    }
    
    @Test
    void testExecuteInstructionWithInvalidInstruction() {
        // Arrange
        String instruction = "UNKNOWN A 10";
        doThrow(new InvalidInstructionException("Invalid instruction"))
            .when(processor).executeInstruction(instruction);
        
        // Act & Assert
        assertThrows(InvalidInstructionException.class, () -> {
            service.executeInstruction(instruction);
        });
    }
    
    @Test
    void testGetAllRegisters() {
        // Arrange
        Map<String, Integer> expectedRegisters = new HashMap<>();
        expectedRegisters.put("A", 1);
        expectedRegisters.put("B", 2);
        expectedRegisters.put("C", 3);
        expectedRegisters.put("D", 4);
        
        when(processor.getAllRegisterValues()).thenReturn(expectedRegisters);
        
        // Act
        Map<String, Integer> result = service.getAllRegisters();
        
        // Assert
        assertAll(
            () -> verify(processor).getAllRegisterValues(),
            () -> assertEquals(expectedRegisters, result)
        );
    }
    
    @Test
    void testGetRegisterValue() {
        // Arrange
        String register = "A";
        int expectedValue = 42;
        
        when(processor.getRegisterValue(register)).thenReturn(expectedValue);
        
        // Act
        Integer result = service.getRegisterValue(register);
        
        // Assert
        assertAll(
            () -> verify(processor).getRegisterValue(register),
            () -> assertEquals(expectedValue, result)
        );
    }
    
    @Test
    void testGetRegisterValueWithInvalidRegister() {
        // Arrange
        String register = "X";
        when(processor.getRegisterValue(register))
            .thenThrow(new InvalidRegisterException("Invalid register"));
        
        // Act & Assert
        assertThrows(InvalidRegisterException.class, () -> {
            service.getRegisterValue(register);
        });
    }
    
    @Test
    void testExecuteBatchInstructions() {
        // Arrange
        List<String> instructions = Arrays.asList("SET A 10", "SET B 20", "ADR A B");
        Map<String, Integer> expectedRegisters = new HashMap<>();
        expectedRegisters.put("A", 30);
        expectedRegisters.put("B", 20);
        expectedRegisters.put("C", 0);
        expectedRegisters.put("D", 0);
        
        when(processor.getAllRegisterValues()).thenReturn(expectedRegisters);
        
        // Act
        Map<String, Integer> result = service.executeBatchInstructions(instructions);
        
        // Assert
        assertAll(
            () -> verify(processor).executeInstruction("SET A 10"),
            () -> verify(processor).executeInstruction("SET B 20"),
            () -> verify(processor).executeInstruction("ADR A B"),
            () -> verify(processor).getAllRegisterValues(),
            () -> assertEquals(expectedRegisters, result)
        );
    }
    
    @Test
    void testExecuteBatchInstructionsWithInvalidInstruction() {
        // Arrange
        List<String> instructions = Arrays.asList("SET A 10", "INVALID B 20");
        doThrow(new InvalidInstructionException("Unknown instruction: INVALID"))
            .when(processor).executeInstruction("INVALID B 20");
        
        // Act & Assert
        BatchExecutionException exception = assertThrows(BatchExecutionException.class, () -> {
            service.executeBatchInstructions(instructions);
        });
        
        assertAll(
            () -> assertEquals(1, exception.getExecutedInstructions()),
            () -> verify(processor).executeInstruction("SET A 10"),
            () -> verify(processor).executeInstruction("INVALID B 20"),
            () -> verify(processor, never()).executeInstruction("ADR A B")
        );
    }
    
    @Test
    void testExecuteBatchInstructionsWithInvalidRegister() {
        // Arrange
        List<String> instructions = Arrays.asList("SET A 10", "SET X 20");
        doThrow(new InvalidRegisterException("Invalid register: X"))
            .when(processor).executeInstruction("SET X 20");
        
        // Act & Assert
        BatchExecutionException exception = assertThrows(BatchExecutionException.class, () -> {
            service.executeBatchInstructions(instructions);
        });
        
        assertAll(
            () -> assertEquals(1, exception.getExecutedInstructions()),
            () -> verify(processor).executeInstruction("SET A 10"),
            () -> verify(processor).executeInstruction("SET X 20"),
            () -> verify(processor, never()).executeInstruction("ADR A B")
        );
    }
}
