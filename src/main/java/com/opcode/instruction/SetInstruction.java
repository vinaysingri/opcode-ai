package com.opcode.instruction;

import com.opcode.core.RegisterManager;
import com.opcode.exception.InvalidSyntaxException;

/**
 * Instruction to set a register to a specific value.
 * Example: SET A 10 (sets register A to 10)
 */
public class SetInstruction extends AbstractInstruction {
    
    /**
     * Constructs a new SetInstruction with the specified arguments.
     *
     * @param args the instruction arguments (register name and value)
     */
    public SetInstruction(String[] args) {
        super(args);
    }
    
    @Override
    public void execute(RegisterManager registerManager) {
        if (!validate()) {
            throw new InvalidSyntaxException("Invalid SET instruction syntax");
        }
        
        String register = args[0];
        validateRegister(register, registerManager);
        
        try {
            int value = Integer.parseInt(args[1]);
            registerManager.setValue(register, value);
        } catch (NumberFormatException e) {
            throw new InvalidSyntaxException("Invalid value for SET instruction: " + args[1]);
        }
    }
    
    @Override
    public boolean validate() {
        return args != null && args.length == 2 && args[0] != null && args[1] != null;
    }
}
