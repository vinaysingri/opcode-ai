package com.opcode.parser;

import com.opcode.exception.InvalidSyntaxException;
import com.opcode.instruction.Instruction;
import com.opcode.instruction.InstructionFactory;
import org.springframework.stereotype.Component;
import java.util.Arrays;

/**
 * Parser for converting instruction text into instruction objects.
 */
@Component
public class InstructionParser {
    
    private final InstructionFactory factory;
    
    /**
     * Constructs a new InstructionParser with the specified factory.
     *
     * @param factory the factory to use for creating instruction objects
     */
    public InstructionParser(InstructionFactory factory) {
        this.factory = factory;
    }
    
    /**
     * Parses an instruction text into an instruction object.
     *
     * @param instructionText the text to parse (e.g., "SET A 10", "ADR C D")
     * @return the parsed instruction object
     * @throws InvalidSyntaxException if the instruction syntax is invalid
     */
    public Instruction parse(String instructionText) {
        if (!validateSyntax(instructionText)) {
            throw new InvalidSyntaxException("Invalid instruction syntax: " + instructionText);
        }
        
        String[] parts = instructionText.trim().split("\\s+");
        String instructionType = parts[0];
        String[] args = Arrays.copyOfRange(parts, 1, parts.length);
        
        return factory.createInstruction(instructionType, args);
    }
    
    /**
     * Validates the basic syntax of an instruction text.
     *
     * @param instructionText the text to validate
     * @return true if the syntax is valid, false otherwise
     */
    private boolean validateSyntax(String instructionText) {
        if (instructionText == null || instructionText.trim().isEmpty()) {
            return false;
        }
        
        String[] parts = instructionText.trim().split("\\s+");
        if (parts.length == 0) {
            return false;
        }
        
        // RST instruction should have no arguments
        if (parts[0].equalsIgnoreCase("RST") && parts.length > 1) {
            return false;
        }
        
        // INR and DCR should have exactly one argument
        if ((parts[0].equalsIgnoreCase("INR") || parts[0].equalsIgnoreCase("DCR")) 
            && parts.length != 2) {
            return false;
        }
        
        // SET, ADR, ADD, MOV should have exactly two arguments
        if ((parts[0].equalsIgnoreCase("SET") || parts[0].equalsIgnoreCase("ADR") 
            || parts[0].equalsIgnoreCase("ADD") || parts[0].equalsIgnoreCase("MOV")) 
            && parts.length != 3) {
            return false;
        }
        
        return true;
    }
}
