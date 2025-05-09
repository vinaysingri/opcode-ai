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
 * Integration tests for the register value endpoints.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RegisterValueIntegrationTest {
    
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
    public void testGetAllRegisters() {
        // Act
        ResponseEntity<Map> response = restTemplate.getForEntity("/api/v1/registers", Map.class);
        
        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        
        Map<String, Integer> registers = response.getBody();
        assertEquals(42, registers.get("A"));
        assertEquals(24, registers.get("B"));
        assertEquals(0, registers.get("C"));
        assertEquals(0, registers.get("D"));
    }
    
    @Test
    public void testGetRegisterValue() {
        // Act
        ResponseEntity<Map> response = restTemplate.getForEntity("/api/v1/registers/A", Map.class);
        
        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(42, response.getBody().get("value"));
    }
    
    @Test
    public void testGetRegisterValue_InvalidRegister() {
        // Act
        ResponseEntity<Map> response = restTemplate.getForEntity("/api/v1/registers/X", Map.class);
        
        // Assert
        assertEquals(404, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("error", response.getBody().get("status"));
        assertTrue(((String) response.getBody().get("message")).contains("Invalid register: X"));
    }
    
    @Test
    public void testGetRegisterValueAfterModification() {
        // Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        InstructionRequest request = new InstructionRequest("ADR A B");
        HttpEntity<InstructionRequest> entity = new HttpEntity<>(request, headers);
        restTemplate.postForEntity("/api/v1/instructions", entity, Map.class);
        
        // Act
        ResponseEntity<Map> response = restTemplate.getForEntity("/api/v1/registers/A", Map.class);
        
        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(66, response.getBody().get("value")); // 42 + 24 = 66
    }
}
