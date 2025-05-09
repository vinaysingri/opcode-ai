package com.opcode.instruction;

import com.opcode.core.RegisterManager;
import com.opcode.exception.InvalidSyntaxException;

/**
 * Instruction to copy the value from one register to another.
 * Example: MOV A B (sets A to the value of B, B remains unchanged)
 */
public class MovInstruction extends AbstractInstruction {
    
    /**
     * Constructs a new MovInstruction with the specified arguments.
     *
     * @param args the instruction arguments (target and source register names)
     */
    public MovInstruction(String[] args) {
        super(args);
    }
    
    @Override
    public void execute(RegisterManager registerManager) {
        if (!validate()) {
            throw new InvalidSyntaxException("Invalid MOV instruction syntax");
        }
        
        String targetRegister = args[0];
        String sourceRegister = args[1];
        
        validateRegister(targetRegister, registerManager);
        validateRegister(sourceRegister, registerManager);
        
        int sourceValue = registerManager.getValue(sourceRegister);
        registerManager.setValue(targetRegister, sourceValue);
    }
    
    @Override
    public boolean validate() {
        return args != null && args.length == 2 && args[0] != null && args[1] != null;
    }
}
