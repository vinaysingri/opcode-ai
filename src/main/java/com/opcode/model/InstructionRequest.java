package com.opcode.model;

import jakarta.validation.constraints.NotBlank;

/**
 * Request model for executing a single instruction.
 */
public class InstructionRequest {
    
    @NotBlank(message = "Instruction cannot be blank")
    private String instruction;
    
    // Default constructor for JSON deserialization
    public InstructionRequest() {
    }
    
    /**
     * Constructs a new InstructionRequest with the specified instruction.
     *
     * @param instruction the instruction text
     */
    public InstructionRequest(String instruction) {
        this.instruction = instruction;
    }
    
    /**
     * Gets the instruction text.
     *
     * @return the instruction text
     */
    public String getInstruction() {
        return instruction;
    }
    
    /**
     * Sets the instruction text.
     *
     * @param instruction the instruction text to set
     */
    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }
}
