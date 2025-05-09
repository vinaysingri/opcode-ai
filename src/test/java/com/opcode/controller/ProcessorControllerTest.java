package com.opcode.controller;

import com.opcode.exception.BatchExecutionException;
import com.opcode.exception.InvalidInstructionException;
import com.opcode.exception.InvalidRegisterException;
import com.opcode.exception.InvalidSyntaxException;
import com.opcode.model.BatchInstructionRequest;
import com.opcode.model.InstructionRequest;
import com.opcode.model.ProcessorResponse;
import com.opcode.service.ProcessorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for the ProcessorController class.
 */
@WebMvcTest(ProcessorController.class)
public class ProcessorControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private ProcessorService processorService;
    
    @Test
    void testExecuteInstruction() throws Exception {
        // Arrange
        Map<String, Integer> registers = new HashMap<>();
        registers.put("A", 42);
        registers.put("B", 0);
        registers.put("C", 0);
        registers.put("D", 0);
        
        when(processorService.executeInstruction("SET A 42")).thenReturn(registers);
        
        // Act & Assert
        mockMvc.perform(post("/api/v1/instructions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"instruction\":\"SET A 42\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.registers.A").value(42))
                .andExpect(jsonPath("$.registers.B").value(0))
                .andExpect(jsonPath("$.registers.C").value(0))
                .andExpect(jsonPath("$.registers.D").value(0));
    }
    
    @Test
    void testExecuteInstructionWithInvalidSyntax() throws Exception {
        // Arrange
        when(processorService.executeInstruction(anyString()))
            .thenThrow(new InvalidSyntaxException("Invalid syntax"));
        
        // Act & Assert
        mockMvc.perform(post("/api/v1/instructions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"instruction\":\"INVALID\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("Invalid syntax"));
    }
    
    @Test
    void testExecuteInstructionWithInvalidRegister() throws Exception {
        // Arrange
        when(processorService.executeInstruction(anyString()))
            .thenThrow(new InvalidRegisterException("Invalid register: X"));
        
        // Act & Assert
        mockMvc.perform(post("/api/v1/instructions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"instruction\":\"SET X 10\"}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("Invalid register: X"));
    }
    
    @Test
    void testExecuteInstructionWithInvalidInstruction() throws Exception {
        // Arrange
        when(processorService.executeInstruction(anyString()))
            .thenThrow(new InvalidInstructionException("Unknown instruction: XYZ"));
        
        // Act & Assert
        mockMvc.perform(post("/api/v1/instructions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"instruction\":\"XYZ A 10\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("Unknown instruction: XYZ"));
    }
    
    @Test
    void testExecuteInstructionWithEmptyRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/v1/instructions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"instruction\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("instruction: Instruction cannot be blank"));
    }
    
    @Test
    void testExecuteInstructionWithMissingInstruction() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/v1/instructions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("instruction: Instruction cannot be blank"));
    }
    
    @Test
    void testExecuteInstructionWithInvalidJson() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/v1/instructions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("invalid json"))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void testExecuteBatchInstructions() throws Exception {
        // Arrange
        List<String> instructions = Arrays.asList("SET A 10", "SET B 20", "ADR A B");
        Map<String, Integer> registers = new HashMap<>();
        registers.put("A", 30);
        registers.put("B", 20);
        registers.put("C", 0);
        registers.put("D", 0);
        
        when(processorService.executeBatchInstructions(instructions)).thenReturn(registers);
        
        // Act & Assert
        mockMvc.perform(post("/api/v1/instructions/batch")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"instructions\":[\"SET A 10\",\"SET B 20\",\"ADR A B\"]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.registers.A").value(30))
                .andExpect(jsonPath("$.registers.B").value(20))
                .andExpect(jsonPath("$.registers.C").value(0))
                .andExpect(jsonPath("$.registers.D").value(0));
        
        verify(processorService).executeBatchInstructions(instructions);
    }
    
    @Test
    void testExecuteBatchInstructionsWithError() throws Exception {
        // Arrange
        List<String> instructions = Arrays.asList("SET A 10", "INVALID B 20");
        when(processorService.executeBatchInstructions(instructions))
            .thenThrow(new BatchExecutionException("Error executing instruction at index 1: INVALID B 20", 1));
        
        // Act & Assert
        mockMvc.perform(post("/api/v1/instructions/batch")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"instructions\":[\"SET A 10\",\"INVALID B 20\"]}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.executedInstructions").value(1));
        
        verify(processorService).executeBatchInstructions(instructions);
    }
    
    @Test
    void testExecuteBatchInstructionsWithEmptyList() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/v1/instructions/batch")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"instructions\":[]}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("instructions: Instructions list cannot be empty"));
    }
    
    @Test
    void testGetAllRegisters() throws Exception {
        // Arrange
        Map<String, Integer> registers = new HashMap<>();
        registers.put("A", 10);
        registers.put("B", 20);
        registers.put("C", 0);
        registers.put("D", 0);
        
        when(processorService.getAllRegisters()).thenReturn(registers);
        
        // Act & Assert
        mockMvc.perform(get("/api/v1/registers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.A").value(10))
                .andExpect(jsonPath("$.B").value(20))
                .andExpect(jsonPath("$.C").value(0))
                .andExpect(jsonPath("$.D").value(0));
        
        verify(processorService).getAllRegisters();
    }
    
    @Test
    void testGetRegisterValue() throws Exception {
        // Arrange
        when(processorService.getRegisterValue("A")).thenReturn(42);
        
        // Act & Assert
        mockMvc.perform(get("/api/v1/registers/A"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.value").value(42));
        
        verify(processorService).getRegisterValue("A");
    }
    
    @Test
    void testGetRegisterValue_InvalidRegister() throws Exception {
        // Arrange
        when(processorService.getRegisterValue("X"))
            .thenThrow(new InvalidRegisterException("Invalid register: X"));
        
        // Act & Assert
        mockMvc.perform(get("/api/v1/registers/X"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("Invalid register: X"));
        
        verify(processorService).getRegisterValue("X");
    }
}
