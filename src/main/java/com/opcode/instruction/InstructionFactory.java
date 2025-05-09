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
        
        switch (type.toUpperCase()) {
            case "SET":
                return new SetInstruction(args);
            case "ADR":
                return new AdrInstruction(args);
            case "ADD":
                return new AddInstruction(args);
            case "MOV":
                return new MovInstruction(args);
            case "INR":
                return new InrInstruction(args);
            case "DCR":
                return new DcrInstruction(args);
            case "RST":
                return new RstInstruction(args);
            default:
                throw new InvalidInstructionException("Unknown instruction type: " + type);
        }
    }
}
