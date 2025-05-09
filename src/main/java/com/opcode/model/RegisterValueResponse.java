package com.opcode.model;

/**
 * Response model for returning a single register value.
 */
public class RegisterValueResponse {
    
    private int value;
    
    /**
     * Default constructor for JSON serialization.
     */
    public RegisterValueResponse() {
    }
    
    /**
     * Constructs a new RegisterValueResponse with the specified value.
     *
     * @param value the register value
     */
    public RegisterValueResponse(int value) {
        this.value = value;
    }
    
    /**
     * Gets the register value.
     *
     * @return the register value
     */
    public int getValue() {
        return value;
    }
    
    /**
     * Sets the register value.
     *
     * @param value the register value to set
     */
    public void setValue(int value) {
        this.value = value;
    }
}
