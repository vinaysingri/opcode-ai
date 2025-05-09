package com.opcode.core;

import com.opcode.exception.InvalidRegisterException;
import org.springframework.stereotype.Component;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages the state of the microprocessor's registers.
 */
@Component
public class RegisterManager {
    
    private final Map<String, Integer> registers;
    
    /**
     * Constructs a new RegisterManager and initializes all registers to zero.
     */
    public RegisterManager() {
        registers = new HashMap<>();
        // Initialize registers A, B, C, D with value 0
        registers.put("A", 0);
        registers.put("B", 0);
        registers.put("C", 0);
        registers.put("D", 0);
    }
    
    /**
     * Gets the value of a specific register.
     *
     * @param register the register name
     * @return the value of the register
     * @throws InvalidRegisterException if the register name is invalid
     */
    public Integer getValue(String register) {
        validateRegister(register);
        return registers.get(register);
    }
    
    /**
     * Sets the value of a specific register.
     *
     * @param register the register name
     * @param value the value to set
     * @throws InvalidRegisterException if the register name is invalid
     */
    public void setValue(String register, Integer value) {
        validateRegister(register);
        registers.put(register, value);
    }
    
    /**
     * Resets all registers to zero.
     */
    public void reset() {
        registers.replaceAll((k, v) -> 0);
    }
    
    /**
     * Gets an unmodifiable view of all registers and their values.
     *
     * @return map of register names to their values
     */
    public Map<String, Integer> getAllRegisters() {
        return Collections.unmodifiableMap(registers);
    }
    
    /**
     * Checks if a register name is valid.
     *
     * @param register the register name to check
     * @return true if the register is valid, false otherwise
     */
    public boolean isValidRegister(String register) {
        return registers.containsKey(register);
    }
    
    /**
     * Validates a register name.
     *
     * @param register the register name to validate
     * @throws InvalidRegisterException if the register name is invalid
     */
    private void validateRegister(String register) {
        if (!isValidRegister(register)) {
            throw new InvalidRegisterException("Invalid register: " + register);
        }
    }
}
