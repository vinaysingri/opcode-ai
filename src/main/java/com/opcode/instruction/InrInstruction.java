package com.opcode.instruction;

import com.opcode.core.RegisterManager;
import com.opcode.exception.InvalidSyntaxException;

/**
 * Instruction to increment a register by 1.
 * Example: INR C (increments C by 1)
 */
public class InrInstruction extends AbstractInstruction {
    
    /**
     * Constructs a new InrInstruction with the specified arguments.
     *
     * @param args the instruction arguments (register name)
     */
    public InrInstruction(String[] args) {
        super(args);
    }
    
    @Override
    public void execute(RegisterManager registerManager) {
        if (!validate()) {
            throw new InvalidSyntaxException("Invalid INR instruction syntax");
        }
        
        String register = args[0];
        validateRegister(register, registerManager);
        
        int currentValue = registerManager.getValue(register);
        registerManager.setValue(register, currentValue + 1);
    }
    
    @Override
    public boolean validate() {
        return args != null && args.length == 1 && args[0] != null;
    }
}
