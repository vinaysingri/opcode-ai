package com.opcode.core;

import com.opcode.instruction.Instruction;
import com.opcode.parser.InstructionParser;
import org.springframework.stereotype.Component;
import java.util.Map;

/**
 * Facade for the microprocessor simulator.
 * Coordinates parsing and execution of instructions and provides access to register values.
 */
@Component
public class Processor {
    
    private final RegisterManager registerManager;
    private final InstructionParser parser;
    
    /**
     * Constructs a new Processor with the specified dependencies.
     *
     * @param registerManager the register manager to use
     * @param parser the instruction parser to use
     */
    public Processor(RegisterManager registerManager, InstructionParser parser) {
        this.registerManager = registerManager;
        this.parser = parser;
    }
    
    /**
     * Executes a single instruction.
     *
     * @param instructionText the instruction to execute (e.g., "SET A 10", "ADR C D")
     */
    public void executeInstruction(String instructionText) {
        Instruction instruction = parser.parse(instructionText);
        instruction.execute(registerManager);
    }
    
    /**
     * Gets the value of a specific register.
     *
     * @param register the register name
     * @return the value of the register
     */
    public Integer getRegisterValue(String register) {
        return registerManager.getValue(register);
    }
    
    /**
     * Gets the values of all registers.
     *
     * @return a map of register names to their values
     */
    public Map<String, Integer> getAllRegisterValues() {
        return registerManager.getAllRegisters();
    }
}
