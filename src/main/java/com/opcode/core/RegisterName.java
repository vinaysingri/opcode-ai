package com.opcode.core;

/**
 * Enum representing valid register names in the microprocessor.
 */
public enum RegisterName {
    A, B, C, D;

    /**
     * Converts a string to a RegisterName.
     *
     * @param name the register name as a string
     * @return the corresponding RegisterName
     * @throws IllegalArgumentException if the name is not a valid register
     */
    public static RegisterName fromString(String name) {
        try {
            return valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid register name: " + name);
        }
    }

    @Override
    public String toString() {
        return name();
    }
}
