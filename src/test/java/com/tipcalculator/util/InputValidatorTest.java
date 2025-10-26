package com.tipcalculator.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("InputValidator Tests")
class InputValidatorTest {
    
    @Test
    @DisplayName("Should parse valid decimal string")
    void testParseDecimal_Valid() {
        BigDecimal result = InputValidator.parseDecimal("123.45");
        assertNotNull(result);
        assertEquals(new BigDecimal("123.45"), result);
    }
    
    @Test
    @DisplayName("Should parse integer string as decimal")
    void testParseDecimal_Integer() {
        BigDecimal result = InputValidator.parseDecimal("100");
        assertNotNull(result);
        assertEquals(new BigDecimal("100"), result);
    }
    
    @Test
    @DisplayName("Should return null for invalid string")
    void testParseDecimal_Invalid() {
        assertNull(InputValidator.parseDecimal("abc"));
        assertNull(InputValidator.parseDecimal("12.34.56"));
    }
    
    @Test
    @DisplayName("Should return null for null input")
    void testParseDecimal_Null() {
        assertNull(InputValidator.parseDecimal(null));
    }
    
    @Test
    @DisplayName("Should return null for empty string")
    void testParseDecimal_Empty() {
        assertNull(InputValidator.parseDecimal(""));
        assertNull(InputValidator.parseDecimal("   "));
    }
    
    @Test
    @DisplayName("Should validate positive numbers")
    void testIsPositive() {
        assertTrue(InputValidator.isPositive(new BigDecimal("0.01")));
        assertTrue(InputValidator.isPositive(new BigDecimal("100")));
        assertFalse(InputValidator.isPositive(BigDecimal.ZERO));
        assertFalse(InputValidator.isPositive(new BigDecimal("-1")));
        assertFalse(InputValidator.isPositive(null));
    }
    
    @Test
    @DisplayName("Should validate non-negative numbers")
    void testIsNonNegative() {
        assertTrue(InputValidator.isNonNegative(BigDecimal.ZERO));
        assertTrue(InputValidator.isNonNegative(new BigDecimal("100")));
        assertFalse(InputValidator.isNonNegative(new BigDecimal("-0.01")));
        assertFalse(InputValidator.isNonNegative(null));
    }
}