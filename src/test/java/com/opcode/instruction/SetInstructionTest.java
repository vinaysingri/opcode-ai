package com.opcode.instruction;

import com.opcode.core.RegisterManager;
import com.opcode.exception.InvalidRegisterException;
import com.opcode.exception.InvalidSyntaxException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the SetInstruction class.
 */
public class SetInstructionTest {
    
    private RegisterManager registerManager;
    
    @BeforeEach
    void setUp() {
        registerManager = new RegisterManager();
    }
    
    @Test
    void testValidSetInstruction() {
        // Arrange
        SetInstruction instruction = new SetInstruction(new String[]{"A", "42"});
        
        // Act
        instruction.execute(registerManager);
        
        // Assert
        assertEquals(42, registerManager.getValue("A"));
        assertEquals(0, registerManager.getValue("B")); // Other registers unchanged
        assertEquals(0, registerManager.getValue("C"));
        assertEquals(0, registerManager.getValue("D"));
    }
    
    @Test
    void testSetNegativeValue() {
        // Arrange
        SetInstruction instruction = new SetInstruction(new String[]{"B", "-123"});
        
        // Act
        instruction.execute(registerManager);
        
        // Assert
        assertEquals(-123, registerManager.getValue("B"));
    }
    
    @Test
    void testInvalidRegister() {
        // Arrange
        SetInstruction instruction = new SetInstruction(new String[]{"X", "10"});
        
        // Act & Assert
        assertThrows(InvalidRegisterException.class, () -> {
            instruction.execute(registerManager);
        });
    }
    
    @Test
    void testInvalidValue() {
        // Arrange
        SetInstruction instruction = new SetInstruction(new String[]{"A", "not_a_number"});
        
        // Act & Assert
        assertThrows(InvalidSyntaxException.class, () -> {
            instruction.execute(registerManager);
        });
    }
    
    @ParameterizedTest
    @MethodSource("provideInvalidArgs")
    void testInvalidArguments(String[] args) {
        // Arrange
        SetInstruction instruction = new SetInstruction(args);
        
        // Act & Assert
        assertThrows(InvalidSyntaxException.class, () -> {
            instruction.execute(registerManager);
        });
    }
    
    private static Stream<Arguments> provideInvalidArgs() {
        return Stream.of(
            Arguments.of((Object) new String[]{null, "10"}),
            Arguments.of((Object) new String[]{"A", null}),
            Arguments.of((Object) new String[]{"A"}),
            Arguments.of((Object) new String[]{"A", "10", "extra"}),
            Arguments.of((Object) new String[]{})
        );
    }
}
