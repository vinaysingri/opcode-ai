package com.opcode.instruction;

import com.opcode.core.RegisterManager;
import com.opcode.exception.InvalidSyntaxException;

/**
 * Instruction to reset all registers to zero.
 * Example: RST (sets all registers A, B, C, D to 0)
 */
public class RstInstruction extends AbstractInstruction {
    
    /**
     * Constructs a new RstInstruction with the specified arguments.
     *
     * @param args the instruction arguments (none required)
     */
    public RstInstruction(String[] args) {
        super(args);
    }
    
    @Override
    public void execute(RegisterManager registerManager) {
        if (!validate()) {
            throw new InvalidSyntaxException("Invalid RST instruction syntax");
        }
        
        registerManager.reset();
    }
    
    @Override
    public boolean validate() {
        return args != null && args.length == 0;
    }
}
