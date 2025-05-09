package com.opcode.exception;

/**
 * Exception thrown when an invalid register is referenced.
 */
public class InvalidRegisterException extends OpcodeException {
    
    /**
     * Constructs a new InvalidRegisterException with the specified detail message.
     *
     * @param message the detail message
     */
    public InvalidRegisterException(String message) {
        super(message);
    }
}
