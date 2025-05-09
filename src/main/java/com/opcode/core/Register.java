package com.opcode.core;

/**
 * Represents a register in the microprocessor.
 * Each register has a name and can store a 32-bit signed integer value.
 */
public class Register {
    private final RegisterName name;
    private int value;

    /**
     * Creates a new register with the specified name and initial value of 0.
     *
     * @param name the register name
     */
    public Register(RegisterName name) {
        this.name = name;
        this.value = 0;
    }

    /**
     * Gets the name of the register.
     *
     * @return the register name
     */
    public RegisterName getName() {
        return name;
    }

    /**
     * Gets the current value stored in the register.
     *
     * @return the register value
     */
    public int getValue() {
        return value;
    }

    /**
     * Sets the value stored in the register.
     *
     * @param value the new value to store
     */
    public void setValue(int value) {
        this.value = value;
    }

    /**
     * Resets the register value to 0.
     */
    public void reset() {
        this.value = 0;
    }

    @Override
    public String toString() {
        return String.format("Register{name=%s, value=%d}", name, value);
    }
}
