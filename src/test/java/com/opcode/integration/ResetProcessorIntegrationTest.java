package com.opcode.integration;

import com.opcode.model.InstructionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for the reset processor endpoint.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ResetProcessorIntegrationTest {
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @BeforeEach
    public void setUp() {
        // Set up some register values
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        InstructionRequest request1 = new InstructionRequest("SET A 42");
        HttpEntity<InstructionRequest> entity1 = new HttpEntity<>(request1, headers);
        restTemplate.postForEntity("/api/v1/instructions", entity1, Map.class);
        
        InstructionRequest request2 = new InstructionRequest("SET B 24");
        HttpEntity<InstructionRequest> entity2 = new HttpEntity<>(request2, headers);
        restTemplate.postForEntity("/api/v1/instructions", entity2, Map.class);
    }
    
    @Test
    public void testResetProcessor() {
        // Verify registers are not zero
        ResponseEntity<Map> getResponse = restTemplate.getForEntity("/api/v1/registers", Map.class);
        Map<String, Integer> registers = getResponse.getBody();
        assertEquals(42, registers.get("A"));
        assertEquals(24, registers.get("B"));
        
        // Reset processor
        ResponseEntity<Map> resetResponse = restTemplate.postForEntity("/api/v1/processor/reset", null, Map.class);
        
        // Verify response
        assertEquals(200, resetResponse.getStatusCodeValue());
        assertNotNull(resetResponse.getBody());
        assertEquals("success", resetResponse.getBody().get("status"));
        
        Map<String, Integer> resetRegisters = (Map<String, Integer>) resetResponse.getBody().get("registers");
        assertEquals(0, resetRegisters.get("A"));
        assertEquals(0, resetRegisters.get("B"));
        assertEquals(0, resetRegisters.get("C"));
        assertEquals(0, resetRegisters.get("D"));
        
        // Verify registers are reset through a separate request
        ResponseEntity<Map> verifyResponse = restTemplate.getForEntity("/api/v1/registers", Map.class);
        Map<String, Integer> verifyRegisters = verifyResponse.getBody();
        assertEquals(0, verifyRegisters.get("A"));
        assertEquals(0, verifyRegisters.get("B"));
        assertEquals(0, verifyRegisters.get("C"));
        assertEquals(0, verifyRegisters.get("D"));
    }
    
    @Test
    public void testResetProcessorAfterMultipleOperations() {
        // Perform multiple operations
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        InstructionRequest request = new InstructionRequest("ADR A B");
        HttpEntity<InstructionRequest> entity = new HttpEntity<>(request, headers);
        restTemplate.postForEntity("/api/v1/instructions", entity, Map.class);
        
        // Verify registers before reset
        ResponseEntity<Map> getResponse = restTemplate.getForEntity("/api/v1/registers", Map.class);
        Map<String, Integer> registers = getResponse.getBody();
        assertEquals(66, registers.get("A")); // 42 + 24 = 66
        assertEquals(24, registers.get("B"));
        
        // Reset processor
        ResponseEntity<Map> resetResponse = restTemplate.postForEntity("/api/v1/processor/reset", null, Map.class);
        
        // Verify all registers are zero
        Map<String, Integer> resetRegisters = (Map<String, Integer>) resetResponse.getBody().get("registers");
        assertEquals(0, resetRegisters.get("A"));
        assertEquals(0, resetRegisters.get("B"));
        assertEquals(0, resetRegisters.get("C"));
        assertEquals(0, resetRegisters.get("D"));
    }
}
