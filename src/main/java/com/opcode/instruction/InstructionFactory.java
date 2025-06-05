package com.opcode.instruction;

import com.opcode.exception.InvalidInstructionException;
import org.springframework.stereotype.Component;

/**
 * Factory for creating instruction objects based on instruction type.
 */
@Component
public class InstructionFactory {
    
    /**
     * Creates an instruction object based on the instruction type and arguments.
     *
     * @param type the type of instruction (e.g., "SET", "ADR", etc.)
     * @param args the arguments for the instruction
     * @return the appropriate instruction object
     * @throws InvalidInstructionException if the instruction type is unknown
     */
    public Instruction createInstruction(String type, String[] args) {
        if (type == null) {
            throw new InvalidInstructionException("Instruction type cannot be null");
        }
        
        return switch (type.toUpperCase()) {
            case "SET" -> new SetInstruction(args);
            case "ADR" -> new AdrInstruction(args);
            case "ADD" -> new AddInstruction(args);
            case "MOV" -> new MovInstruction(args);
            case "INR" -> new InrInstruction(args);
            case "DCR" -> new DcrInstruction(args);
            case "RST" -> new RstInstruction(args);
            default -> throw new InvalidInstructionException("Unknown instruction type: " + type);
        };
    }
}
