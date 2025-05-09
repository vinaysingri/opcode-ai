package com.opcode.model;

import java.util.Map;

/**
 * Standard API response model for processor operations.
 */
public class ProcessorResponse {
    
    private String status;
    private String message;
    private Map<String, Integer> registers;
    private Integer executedInstructions;
    
    // Default constructor for JSON serialization
    public ProcessorResponse() {
    }
    
    /**
     * Gets the status of the response.
     *
     * @return the status ("success" or "error")
     */
    public String getStatus() {
        return status;
    }
    
    /**
     * Sets the status of the response.
     *
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }
    
    /**
     * Gets the message (usually used for error messages).
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }
    
    /**
     * Sets the message.
     *
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }
    
    /**
     * Gets the register values.
     *
     * @return map of register names to their values
     */
    public Map<String, Integer> getRegisters() {
        return registers;
    }
    
    /**
     * Sets the register values.
     *
     * @param registers map of register names to their values
     */
    public void setRegisters(Map<String, Integer> registers) {
        this.registers = registers;
    }
    
    /**
     * Gets the number of instructions executed in a batch operation before an error occurred.
     *
     * @return the number of executed instructions
     */
    public Integer getExecutedInstructions() {
        return executedInstructions;
    }
    
    /**
     * Sets the number of instructions executed in a batch operation before an error occurred.
     *
     * @param executedInstructions the number of executed instructions
     */
    public void setExecutedInstructions(Integer executedInstructions) {
        this.executedInstructions = executedInstructions;
    }
    
    /**
     * Creates a success response with register values.
     *
     * @param registers the register values
     * @return the success response
     */
    public static ProcessorResponse success(Map<String, Integer> registers) {
        ProcessorResponse response = new ProcessorResponse();
        response.setStatus("success");
        response.setRegisters(registers);
        return response;
    }
    
    /**
     * Creates an error response with a message.
     *
     * @param message the error message
     * @return the error response
     */
    public static ProcessorResponse error(String message) {
        ProcessorResponse response = new ProcessorResponse();
        response.setStatus("error");
        response.setMessage(message);
        return response;
    }
    
    /**
     * Creates an error response with a message and the number of instructions executed before the error.
     *
     * @param message the error message
     * @param executedInstructions the number of instructions executed before the error
     * @return the error response
     */
    public static ProcessorResponse batchError(String message, int executedInstructions) {
        ProcessorResponse response = error(message);
        response.setExecutedInstructions(executedInstructions);
        return response;
    }
}
