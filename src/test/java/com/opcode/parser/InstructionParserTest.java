package com.opcode.parser;

import com.opcode.exception.InvalidSyntaxException;
import com.opcode.instruction.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the InstructionParser class.
 */
public class InstructionParserTest {
    
    private InstructionParser parser;
    private InstructionFactory mockFactory;
    
    @BeforeEach
    void setUp() {
        mockFactory = mock(InstructionFactory.class);
        parser = new InstructionParser(mockFactory);
    }
    
    @ParameterizedTest
    @MethodSource("provideValidInstructions")
    void testParseValidInstructions(String instructionText, String expectedType, String[] expectedArgs) {
        // Arrange
        when(mockFactory.createInstruction(eq(expectedType), any())).thenReturn(mock(Instruction.class));
        
        // Act
        parser.parse(instructionText);
        
        // Assert
        verify(mockFactory).createInstruction(expectedType, expectedArgs);
    }
    
    private static Stream<Arguments> provideValidInstructions() {
        return Stream.of(
            Arguments.of("SET A 10", "SET", new String[]{"A", "10"}),
            Arguments.of("ADR A B", "ADR", new String[]{"A", "B"}),
            Arguments.of("ADD C 42", "ADD", new String[]{"C", "42"}),
            Arguments.of("MOV D A", "MOV", new String[]{"D", "A"}),
            Arguments.of("INR B", "INR", new String[]{"B"}),
            Arguments.of("DCR C", "DCR", new String[]{"C"}),
            Arguments.of("RST", "RST", new String[]{})
        );
    }
    
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t", "\n"})
    void testParseInvalidInput(String instructionText) {
        assertThrows(InvalidSyntaxException.class, () -> {
            parser.parse(instructionText);
        });
    }
    
    @Test
    void testParseRstWithArguments() {
        assertThrows(InvalidSyntaxException.class, () -> {
            parser.parse("RST A");
        });
    }
    
    @Test
    void testParseInrWithTooManyArguments() {
        assertThrows(InvalidSyntaxException.class, () -> {
            parser.parse("INR A B");
        });
    }
    
    @Test
    void testParseDcrWithTooManyArguments() {
        assertThrows(InvalidSyntaxException.class, () -> {
            parser.parse("DCR A B");
        });
    }
    
    @Test
    void testParseSetWithTooFewArguments() {
        assertThrows(InvalidSyntaxException.class, () -> {
            parser.parse("SET A");
        });
    }
    
    @Test
    void testParseSetWithTooManyArguments() {
        assertThrows(InvalidSyntaxException.class, () -> {
            parser.parse("SET A 10 20");
        });
    }
    
    @Test
    void testParseWithExtraWhitespace() {
        // Arrange
        when(mockFactory.createInstruction(eq("SET"), any())).thenReturn(mock(Instruction.class));
        
        // Act
        parser.parse("   SET    A    10   ");
        
        // Assert
        verify(mockFactory).createInstruction("SET", new String[]{"A", "10"});
    }
    
    @Test
    void testParseWithMixedCase() {
        // Arrange
        when(mockFactory.createInstruction(eq("SET"), any())).thenReturn(mock(Instruction.class));
        
        // Act
        parser.parse("SET A 10");
        
        // Assert
        verify(mockFactory).createInstruction("SET", new String[]{"A", "10"});
    }
}
