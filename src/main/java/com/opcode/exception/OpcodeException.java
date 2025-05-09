package com.opcode.exception;

/**
 * Base exception class for all Opcode-related exceptions.
 */
public abstract class OpcodeException extends RuntimeException {
    
    /**
     * Constructs a new OpcodeException with the specified detail message.
     *
     * @param message the detail message
     */
    public OpcodeException(String message) {
        super(message);
    }
}
