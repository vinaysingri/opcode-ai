package com.opcode.core;

import com.opcode.exception.InvalidRegisterException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the RegisterManager class.
 */
public class RegisterManagerTest {
    
    private RegisterManager registerManager;
    
    @BeforeEach
    public void setUp() {
        registerManager = new RegisterManager();
    }
    
    @Test
    public void testInitialization() {
        // All registers should be initialized to 0
        assertEquals(0, registerManager.getValue("A"));
        assertEquals(0, registerManager.getValue("B"));
        assertEquals(0, registerManager.getValue("C"));
        assertEquals(0, registerManager.getValue("D"));
    }
    
    @Test
    public void testSetValue() {
        // Set register A to 42
        registerManager.setValue("A", 42);
        assertEquals(42, registerManager.getValue("A"));
        
        // Other registers should remain unchanged
        assertEquals(0, registerManager.getValue("B"));
        assertEquals(0, registerManager.getValue("C"));
        assertEquals(0, registerManager.getValue("D"));
    }
    
    @Test
    public void testReset() {
        // Set some values
        registerManager.setValue("A", 42);
        registerManager.setValue("B", 24);
        
        // Reset all registers
        registerManager.reset();
        
        // All registers should be 0
        assertEquals(0, registerManager.getValue("A"));
        assertEquals(0, registerManager.getValue("B"));
        assertEquals(0, registerManager.getValue("C"));
        assertEquals(0, registerManager.getValue("D"));
    }
    
    @Test
    public void testInvalidRegister() {
        // Getting value of invalid register should throw exception
        assertThrows(InvalidRegisterException.class, () -> {
            registerManager.getValue("X");
        });
        
        // Setting value of invalid register should throw exception
        assertThrows(InvalidRegisterException.class, () -> {
            registerManager.setValue("X", 10);
        });
    }
    
    @Test
    public void testIsValidRegister() {
        // Valid registers
        assertTrue(registerManager.isValidRegister("A"));
        assertTrue(registerManager.isValidRegister("B"));
        assertTrue(registerManager.isValidRegister("C"));
        assertTrue(registerManager.isValidRegister("D"));
        
        // Invalid register
        assertFalse(registerManager.isValidRegister("X"));
    }
    
    @Test
    public void testGetAllRegisters() {
        // Set some values
        registerManager.setValue("A", 1);
        registerManager.setValue("B", 2);
        registerManager.setValue("C", 3);
        registerManager.setValue("D", 4);
        
        // Get all registers
        Map<String, Integer> registers = registerManager.getAllRegisters();
        
        // Check size and contents
        assertEquals(4, registers.size());
        assertEquals(1, registers.get("A"));
        assertEquals(2, registers.get("B"));
        assertEquals(3, registers.get("C"));
        assertEquals(4, registers.get("D"));
        
        // Verify that the returned map is unmodifiable
        assertThrows(UnsupportedOperationException.class, () -> {
            registers.put("E", 5);
        });
    }
}
