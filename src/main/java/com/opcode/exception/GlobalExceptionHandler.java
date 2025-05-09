package com.opcode.exception;

import com.opcode.model.ProcessorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;

/**
 * Global exception handler for the application.
 * Provides consistent error responses for different types of exceptions.
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    
    /**
     * Handles invalid register exceptions.
     *
     * @param ex the exception
     * @return the error response
     */
    @ExceptionHandler(InvalidRegisterException.class)
    public ResponseEntity<ProcessorResponse> handleInvalidRegisterException(InvalidRegisterException ex) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ProcessorResponse.error(ex.getMessage()));
    }
    
    /**
     * Handles invalid instruction exceptions.
     *
     * @param ex the exception
     * @return the error response
     */
    @ExceptionHandler(InvalidInstructionException.class)
    public ResponseEntity<ProcessorResponse> handleInvalidInstructionException(InvalidInstructionException ex) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ProcessorResponse.error(ex.getMessage()));
    }
    
    /**
     * Handles invalid syntax exceptions.
     *
     * @param ex the exception
     * @return the error response
     */
    @ExceptionHandler(InvalidSyntaxException.class)
    public ResponseEntity<ProcessorResponse> handleInvalidSyntaxException(InvalidSyntaxException ex) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ProcessorResponse.error(ex.getMessage()));
    }
    
    /**
     * Handles validation exceptions.
     *
     * @param ex the exception
     * @return the error response
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProcessorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .findFirst()
            .orElse("Validation error");
            
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ProcessorResponse.error(message));
    }
    
    /**
     * Handles JSON parsing exceptions.
     *
     * @param ex the exception
     * @return the error response
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ProcessorResponse> handleMessageNotReadableException(HttpMessageNotReadableException ex) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ProcessorResponse.error("Invalid JSON format: " + ex.getMessage()));
    }
    
    /**
     * Handles all other exceptions.
     *
     * @param ex the exception
     * @return the error response
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProcessorResponse> handleGenericException(Exception ex) {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ProcessorResponse.error("An unexpected error occurred: " + ex.getMessage()));
    }
}
