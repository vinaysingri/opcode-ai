package com.opcode.instruction;

import com.opcode.core.RegisterManager;
import com.opcode.exception.InvalidRegisterException;

/**
 * Abstract base class for common instruction functionality.
 * All specific instruction implementations should extend this class.
 */
public abstract class AbstractInstruction implements Instruction {
    
    /**
     * The arguments for the instruction.
     */
    protected final String[] args;
    
    /**
     * Constructs a new AbstractInstruction with the specified arguments.
     *
     * @param args the instruction arguments
     */
    public AbstractInstruction(String[] args) {
        this.args = args;
    }
    
    /**
     * Executes the instruction on the given register manager.
     * This method must be implemented by subclasses.
     *
     * @param registerManager the register manager to execute the instruction on
     */
    @Override
    public abstract void execute(RegisterManager registerManager);
    
    /**
     * Validates the instruction syntax.
     * This method must be implemented by subclasses.
     *
     * @return true if the instruction is valid, false otherwise
     */
    @Override
    public abstract boolean validate();
    
    /**
     * Validates that the specified register name is valid.
     *
     * @param register the register name to validate
     * @param registerManager the register manager to validate against
     * @throws InvalidRegisterException if the register name is invalid
     */
    protected void validateRegister(String register, RegisterManager registerManager) {
        if (!registerManager.isValidRegister(register)) {
            throw new InvalidRegisterException("Invalid register: " + register);
        }
    }
}
