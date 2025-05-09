package com.opcode.integration;

import com.opcode.model.BatchInstructionRequest;
import com.opcode.model.InstructionRequest;
import com.opcode.model.ProcessorResponse;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for the Opcode Microprocessor Simulator.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProcessorIntegrationTest {
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Test
    void testExecuteSetInstruction() {
        // Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        InstructionRequest request = new InstructionRequest("SET A 42");
        HttpEntity<InstructionRequest> entity = new HttpEntity<>(request, headers);
        
        // Act
        ResponseEntity<ProcessorResponse> response = restTemplate.postForEntity(
            "/api/v1/instructions",
            entity,
            ProcessorResponse.class
        );
        
        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertEquals("success", response.getBody().getStatus());
        assertEquals(42, response.getBody().getRegisters().get("A"));
        assertEquals(0, response.getBody().getRegisters().get("B"));
        assertEquals(0, response.getBody().getRegisters().get("C"));
        assertEquals(0, response.getBody().getRegisters().get("D"));
    }
    
    @Test
    void testExecuteBatchInstructions() {
        // Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        List<String> instructions = Arrays.asList("SET A 10", "SET B 20", "ADR A B");
        BatchInstructionRequest request = new BatchInstructionRequest(instructions);
        HttpEntity<BatchInstructionRequest> entity = new HttpEntity<>(request, headers);
        
        // Act
        ResponseEntity<ProcessorResponse> response = restTemplate.postForEntity(
            "/api/v1/instructions/batch",
            entity,
            ProcessorResponse.class
        );
        
        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertEquals("success", response.getBody().getStatus());
        assertEquals(30, response.getBody().getRegisters().get("A"));
        assertEquals(20, response.getBody().getRegisters().get("B"));
        assertEquals(0, response.getBody().getRegisters().get("C"));
        assertEquals(0, response.getBody().getRegisters().get("D"));
    }
    
    @Test
    void testExecuteBatchInstructionsWithError() {
        // Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        List<String> instructions = Arrays.asList("SET A 10", "INVALID B 20");
        BatchInstructionRequest request = new BatchInstructionRequest(instructions);
        HttpEntity<BatchInstructionRequest> entity = new HttpEntity<>(request, headers);
        
        // Act
        ResponseEntity<ProcessorResponse> response = restTemplate.postForEntity(
            "/api/v1/instructions/batch",
            entity,
            ProcessorResponse.class
        );
        
        // Assert
        assertTrue(response.getStatusCode().is4xxClientError());
        assertNotNull(response.getBody());
        assertEquals("error", response.getBody().getStatus());
        assertNotNull(response.getBody().getMessage());
        assertEquals(1, response.getBody().getExecutedInstructions());
    }
    
    @Test
    void testExecuteBatchInstructionsWithEmptyList() {
        // Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        BatchInstructionRequest request = new BatchInstructionRequest(Arrays.asList());
        HttpEntity<BatchInstructionRequest> entity = new HttpEntity<>(request, headers);
        
        // Act
        ResponseEntity<ProcessorResponse> response = restTemplate.postForEntity(
            "/api/v1/instructions/batch",
            entity,
            ProcessorResponse.class
        );
        
        // Assert
        assertTrue(response.getStatusCode().is4xxClientError());
        assertNotNull(response.getBody());
        assertEquals("error", response.getBody().getStatus());
        assertTrue(response.getBody().getMessage().contains("Instructions list cannot be empty"));
    }
    
    @Test
    void testExecuteMultipleInstructions() {
        // Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        // Act & Assert - SET A 10
        InstructionRequest request1 = new InstructionRequest("SET A 10");
        HttpEntity<InstructionRequest> entity1 = new HttpEntity<>(request1, headers);
        ResponseEntity<ProcessorResponse> response1 = restTemplate.postForEntity(
            "/api/v1/instructions",
            entity1,
            ProcessorResponse.class
        );
        assertTrue(response1.getStatusCode().is2xxSuccessful());
        assertEquals(10, response1.getBody().getRegisters().get("A"));
        
        // Act & Assert - SET B 20
        InstructionRequest request2 = new InstructionRequest("SET B 20");
        HttpEntity<InstructionRequest> entity2 = new HttpEntity<>(request2, headers);
        ResponseEntity<ProcessorResponse> response2 = restTemplate.postForEntity(
            "/api/v1/instructions",
            entity2,
            ProcessorResponse.class
        );
        assertTrue(response2.getStatusCode().is2xxSuccessful());
        assertEquals(10, response2.getBody().getRegisters().get("A"));
        assertEquals(20, response2.getBody().getRegisters().get("B"));
        
        // Act & Assert - ADR A B
        InstructionRequest request3 = new InstructionRequest("ADR A B");
        HttpEntity<InstructionRequest> entity3 = new HttpEntity<>(request3, headers);
        ResponseEntity<ProcessorResponse> response3 = restTemplate.postForEntity(
            "/api/v1/instructions",
            entity3,
            ProcessorResponse.class
        );
        assertTrue(response3.getStatusCode().is2xxSuccessful());
        assertEquals(30, response3.getBody().getRegisters().get("A"));
        assertEquals(20, response3.getBody().getRegisters().get("B"));
    }
    
    @Test
    void testExecuteInvalidInstruction() {
        // Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        InstructionRequest request = new InstructionRequest("INVALID A 10");
        HttpEntity<InstructionRequest> entity = new HttpEntity<>(request, headers);
        
        // Act
        ResponseEntity<ProcessorResponse> response = restTemplate.postForEntity(
            "/api/v1/instructions",
            entity,
            ProcessorResponse.class
        );
        
        // Assert
        assertTrue(response.getStatusCode().is4xxClientError());
        assertNotNull(response.getBody());
        assertEquals("error", response.getBody().getStatus());
        assertNotNull(response.getBody().getMessage());
    }
    
    @Test
    void testExecuteInvalidRegister() {
        // Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        InstructionRequest request = new InstructionRequest("SET X 10");
        HttpEntity<InstructionRequest> entity = new HttpEntity<>(request, headers);
        
        // Act
        ResponseEntity<ProcessorResponse> response = restTemplate.postForEntity(
            "/api/v1/instructions",
            entity,
            ProcessorResponse.class
        );
        
        // Assert
        assertTrue(response.getStatusCode().is4xxClientError());
        assertNotNull(response.getBody());
        assertEquals("error", response.getBody().getStatus());
        assertTrue(response.getBody().getMessage().contains("Invalid register"));
    }
    
    @Test
    void testRstInstruction() {
        // Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        // First set some values
        restTemplate.postForEntity(
            "/api/v1/instructions",
            new HttpEntity<>(new InstructionRequest("SET A 10"), headers),
            ProcessorResponse.class
        );
        restTemplate.postForEntity(
            "/api/v1/instructions",
            new HttpEntity<>(new InstructionRequest("SET B 20"), headers),
            ProcessorResponse.class
        );
        
        // Then reset
        InstructionRequest resetRequest = new InstructionRequest("RST");
        HttpEntity<InstructionRequest> resetEntity = new HttpEntity<>(resetRequest, headers);
        
        // Act
        ResponseEntity<ProcessorResponse> response = restTemplate.postForEntity(
            "/api/v1/instructions",
            resetEntity,
            ProcessorResponse.class
        );
        
        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertEquals("success", response.getBody().getStatus());
        assertEquals(0, response.getBody().getRegisters().get("A"));
        assertEquals(0, response.getBody().getRegisters().get("B"));
        assertEquals(0, response.getBody().getRegisters().get("C"));
        assertEquals(0, response.getBody().getRegisters().get("D"));
    }
}
