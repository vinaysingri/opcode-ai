package com.opcode.instruction;

import com.opcode.core.RegisterManager;
import com.opcode.exception.InvalidSyntaxException;

/**
 * Instruction to add a constant value to a register.
 * Example: ADD A 12 (adds 12 to register A)
 */
public class AddInstruction extends AbstractInstruction {
    
    /**
     * Constructs a new AddInstruction with the specified arguments.
     *
     * @param args the instruction arguments (register name and value to add)
     */
    public AddInstruction(String[] args) {
        super(args);
    }
    
    @Override
    public void execute(RegisterManager registerManager) {
        if (!validate()) {
            throw new InvalidSyntaxException("Invalid ADD instruction syntax");
        }
        
        String register = args[0];
        validateRegister(register, registerManager);
        
        try {
            int valueToAdd = Integer.parseInt(args[1]);
            int currentValue = registerManager.getValue(register);
            registerManager.setValue(register, currentValue + valueToAdd);
        } catch (NumberFormatException e) {
            throw new InvalidSyntaxException("Invalid value for ADD instruction: " + args[1]);
        }
    }
    
    @Override
    public boolean validate() {
        return args != null && args.length == 2 && args[0] != null && args[1] != null;
    }
}
