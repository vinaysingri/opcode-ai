package com.opcode.service;

import com.opcode.core.Processor;
import com.opcode.exception.InvalidInstructionException;
import com.opcode.exception.InvalidRegisterException;
import com.opcode.exception.InvalidSyntaxException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
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
}
