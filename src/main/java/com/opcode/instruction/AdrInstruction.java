package com.opcode.instruction;

import com.opcode.core.RegisterManager;
import com.opcode.exception.InvalidSyntaxException;

/**
 * Instruction to add the value of one register to another.
 * Example: ADR C D (adds content of D into C, result stored in C)
 */
public class AdrInstruction extends AbstractInstruction {
    
    /**
     * Constructs a new AdrInstruction with the specified arguments.
     *
     * @param args the instruction arguments (target and source register names)
     */
    public AdrInstruction(String[] args) {
        super(args);
    }
    
    @Override
    public void execute(RegisterManager registerManager) {
        if (!validate()) {
            throw new InvalidSyntaxException("Invalid ADR instruction syntax");
        }
        
        String targetRegister = args[0];
        String sourceRegister = args[1];
        
        validateRegister(targetRegister, registerManager);
        validateRegister(sourceRegister, registerManager);
        
        int targetValue = registerManager.getValue(targetRegister);
        int sourceValue = registerManager.getValue(sourceRegister);
        
        registerManager.setValue(targetRegister, targetValue + sourceValue);
    }
    
    @Override
    public boolean validate() {
        return args != null && args.length == 2 && args[0] != null && args[1] != null;
    }
}
