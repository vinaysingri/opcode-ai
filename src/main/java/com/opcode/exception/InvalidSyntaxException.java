package com.opcode.exception;

/**
 * Exception thrown when invalid syntax is encountered in an instruction.
 */
public class InvalidSyntaxException extends OpcodeException {
    
    /**
     * Constructs a new InvalidSyntaxException with the specified detail message.
     *
     * @param message the detail message
     */
    public InvalidSyntaxException(String message) {
        super(message);
    }
}
