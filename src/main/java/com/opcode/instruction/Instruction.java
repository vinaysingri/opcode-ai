package com.opcode.instruction;

import com.opcode.core.RegisterManager;

/**
 * Interface defining the contract for all instruction implementations.
 */
public interface Instruction {
    
    /**
     * Executes the instruction on the given register manager.
     *
     * @param registerManager the register manager to execute the instruction on
     */
    void execute(RegisterManager registerManager);
    
    /**
     * Validates the instruction syntax.
     *
     * @return true if the instruction is valid, false otherwise
     */
    boolean validate();
}
