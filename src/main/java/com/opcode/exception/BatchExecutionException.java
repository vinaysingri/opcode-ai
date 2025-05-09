package com.opcode.exception;

/**
 * Exception thrown when an error occurs during batch instruction execution.
 * Tracks the number of instructions that were successfully executed before the error.
 */
public class BatchExecutionException extends OpcodeException {
    
    private final int executedInstructions;
    
    /**
     * Constructs a new BatchExecutionException with the specified message and count of executed instructions.
     *
     * @param message the error message
     * @param executedInstructions the number of instructions successfully executed before the error
     */
    public BatchExecutionException(String message, int executedInstructions) {
        super(message);
        this.executedInstructions = executedInstructions;
    }
    
    /**
     * Gets the number of instructions that were successfully executed before the error.
     *
     * @return the number of executed instructions
     */
    public int getExecutedInstructions() {
        return executedInstructions;
    }
}
