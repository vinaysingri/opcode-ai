package com.opcode.instruction;

import com.opcode.exception.InvalidInstructionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the InstructionFactory class.
 */
public class InstructionFactoryTest {
    
    private InstructionFactory factory;
    
    @BeforeEach
    void setUp() {
        factory = new InstructionFactory();
    }
    
    @ParameterizedTest
    @MethodSource("provideValidInstructions")
    void testCreateValidInstructions(String type, String[] args, Class<? extends Instruction> expectedClass) {
        // Act
        Instruction instruction = factory.createInstruction(type, args);
        
        // Assert
        assertNotNull(instruction);
        assertEquals(expectedClass, instruction.getClass());
    }
    
    private static Stream<Arguments> provideValidInstructions() {
        return Stream.of(
            Arguments.of("SET", new String[]{"A", "10"}, SetInstruction.class),
            Arguments.of("ADR", new String[]{"A", "B"}, AdrInstruction.class),
            Arguments.of("ADD", new String[]{"A", "10"}, AddInstruction.class),
            Arguments.of("MOV", new String[]{"A", "B"}, MovInstruction.class),
            Arguments.of("INR", new String[]{"A"}, InrInstruction.class),
            Arguments.of("DCR", new String[]{"A"}, DcrInstruction.class),
            Arguments.of("RST", new String[]{}, RstInstruction.class)
        );
    }
    
    @Test
    void testCreateInstructionWithNullType() {
        // Act & Assert
        assertThrows(InvalidInstructionException.class, () -> {
            factory.createInstruction(null, new String[]{"A", "10"});
        });
    }
    
    @Test
    void testCreateInstructionWithInvalidType() {
        // Act & Assert
        assertThrows(InvalidInstructionException.class, () -> {
            factory.createInstruction("INVALID", new String[]{"A", "10"});
        });
    }
    
    @Test
    void testCaseInsensitiveInstructionType() {
        // Act
        Instruction instruction1 = factory.createInstruction("set", new String[]{"A", "10"});
        Instruction instruction2 = factory.createInstruction("SET", new String[]{"A", "10"});
        Instruction instruction3 = factory.createInstruction("Set", new String[]{"A", "10"});
        
        // Assert
        assertAll(
            () -> assertTrue(instruction1 instanceof SetInstruction instruction1Cast),
            () -> assertTrue(instruction2 instanceof SetInstruction instruction2Cast),
            () -> assertTrue(instruction3 instanceof SetInstruction instruction3Cast)
        );
    }
    
    @Test
    void testCreateInstructionWithNullArgs() {
        // Act
        Instruction instruction = factory.createInstruction("SET", null);
        
        // Assert
        assertNotNull(instruction);
        assertTrue(instruction instanceof SetInstruction setInstruction);
    }
}
