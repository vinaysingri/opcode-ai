package com.opcode.exception;

/**
 * Exception thrown when an invalid instruction type is encountered.
 */
public class InvalidInstructionException extends OpcodeException {
    
    /**
     * Constructs a new InvalidInstructionException with the specified detail message.
     *
     * @param message the detail message
     */
    public InvalidInstructionException(String message) {
        super(message);
    }
}
