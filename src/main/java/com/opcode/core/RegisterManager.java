package com.opcode.core;

import com.opcode.exception.InvalidRegisterException;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Manages the state of the microprocessor's registers.
 * The microprocessor has 4 registers (A, B, C, D), each capable of storing 32-bit signed integers.
 */
public class RegisterManager {
    private final Map<RegisterName, Register> registers;
    
    /**
     * Constructs a new RegisterManager with all registers initialized to 0.
     */
    public RegisterManager() {
        registers = new EnumMap<>(RegisterName.class);
        // Initialize all registers
        for (RegisterName name : RegisterName.values()) {
            registers.put(name, new Register(name));
        }
    }
    
    /**
     * Gets the value of the specified register.
     *
     * @param registerName the register name
     * @return the value of the register
     * @throws InvalidRegisterException if the register name is invalid
     */
    public Integer getValue(String registerName) {
        RegisterName name = validateAndGetRegisterName(registerName);
        return registers.get(name).getValue();
    }
    
    /**
     * Sets the value of the specified register.
     *
     * @param registerName the register name
     * @param value the value to set
     * @throws InvalidRegisterException if the register name is invalid
     */
    public void setValue(String registerName, Integer value) {
        RegisterName name = validateAndGetRegisterName(registerName);
        registers.get(name).setValue(value);
    }
    
    /**
     * Resets all registers to 0.
     */
    public void reset() {
        registers.values().forEach(Register::reset);
    }
    
    /**
     * Gets an unmodifiable view of all registers and their values.
     *
     * @return an unmodifiable map of register names to values
     */
    public Map<String, Integer> getAllRegisters() {
        return Collections.unmodifiableMap(
            registers.entrySet().stream()
                .collect(Collectors.toMap(
                    e -> e.getKey().toString(),
                    e -> e.getValue().getValue()
                ))
        );
    }
    
    /**
     * Checks if the specified register name is valid.
     *
     * @param registerName the register name to check
     * @return true if the register name is valid, false otherwise
     */
    public boolean isValidRegister(String registerName) {
        try {
            RegisterName.fromString(registerName);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
    
    /**
     * Validates and converts a register name string to RegisterName enum.
     *
     * @param registerName the register name to validate
     * @return the validated RegisterName
     * @throws InvalidRegisterException if the register name is invalid
     */
    private RegisterName validateAndGetRegisterName(String registerName) {
        try {
            return RegisterName.fromString(registerName);
        } catch (IllegalArgumentException e) {
            throw new InvalidRegisterException("Invalid register: " + registerName);
        }
    }
}
