package com.opcode.service;

import com.opcode.core.Processor;
import org.springframework.stereotype.Service;
import java.util.Map;

/**
 * Service layer for the microprocessor simulator.
 * Provides high-level operations for executing instructions and managing processor state.
 */
@Service
public class ProcessorService {
    
    private final Processor processor;
    
    /**
     * Constructs a new ProcessorService with the specified processor.
     *
     * @param processor the processor to use
     */
    public ProcessorService(Processor processor) {
        this.processor = processor;
    }
    
    /**
     * Executes a single instruction and returns the updated register values.
     *
     * @param instructionText the instruction to execute (e.g., "SET A 10", "ADR C D")
     * @return a map of register names to their updated values
     */
    public Map<String, Integer> executeInstruction(String instructionText) {
        processor.executeInstruction(instructionText);
        return processor.getAllRegisterValues();
    }
    
    /**
     * Gets the values of all registers.
     *
     * @return a map of register names to their values
     */
    public Map<String, Integer> getAllRegisters() {
        return processor.getAllRegisterValues();
    }
    
    /**
     * Gets the value of a specific register.
     *
     * @param register the register name
     * @return the value of the register
     */
    public Integer getRegisterValue(String register) {
        return processor.getRegisterValue(register);
    }
}
