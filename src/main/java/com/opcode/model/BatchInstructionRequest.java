package com.opcode.model;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

/**
 * Request model for executing multiple instructions in batch.
 */
public class BatchInstructionRequest {
    
    @NotEmpty(message = "Instructions list cannot be empty")
    private List<String> instructions;
    
    // Default constructor for JSON deserialization
    public BatchInstructionRequest() {
    }
    
    /**
     * Constructs a new BatchInstructionRequest with the specified instructions.
     *
     * @param instructions the list of instructions to execute
     */
    public BatchInstructionRequest(List<String> instructions) {
        this.instructions = instructions;
    }
    
    /**
     * Gets the list of instructions.
     *
     * @return the list of instructions
     */
    public List<String> getInstructions() {
        return instructions;
    }
    
    /**
     * Sets the list of instructions.
     *
     * @param instructions the list of instructions to set
     */
    public void setInstructions(List<String> instructions) {
        this.instructions = instructions;
    }
}
