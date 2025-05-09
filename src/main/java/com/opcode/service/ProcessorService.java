package com.opcode.service;

import com.opcode.core.Processor;
import com.opcode.exception.BatchExecutionException;
import org.springframework.stereotype.Service;
import java.util.List;
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
     * Executes multiple instructions in sequence and returns the updated register values.
     * If an error occurs during execution, a BatchExecutionException is thrown with details
     * about how many instructions were successfully executed.
     *
     * @param instructions the list of instructions to execute
     * @return a map of register names to their updated values
     * @throws BatchExecutionException if an error occurs during batch execution
     */
    public Map<String, Integer> executeBatchInstructions(List<String> instructions) {
        int executedCount = 0;
        try {
            for (String instruction : instructions) {
                // Increment count before execution to include the failing instruction
                executedCount++;
                processor.executeInstruction(instruction);
            }
            return processor.getAllRegisterValues();
        } catch (Exception e) {
            // Decrement count since the last instruction failed
            executedCount--;
            throw new BatchExecutionException(
                "Error executing instruction at index " + executedCount + ": " + instructions.get(executedCount) + " - " + e.getMessage(),
                executedCount
            );
        }
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
    
    /**
     * Resets all registers to zero by executing the RST instruction.
     *
     * @return a map of register names to their values (all zero)
     */
    public Map<String, Integer> resetProcessor() {
        processor.executeInstruction("RST");
        return processor.getAllRegisterValues();
    }
}
