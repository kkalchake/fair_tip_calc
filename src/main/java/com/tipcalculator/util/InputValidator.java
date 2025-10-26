package com.tipcalculator.util;

import java.math.BigDecimal;

/**
 * Utility class for validating user inputs.
 */
public final class InputValidator {
    
    private InputValidator() {
        // Prevent instantiation
    }
    
    public static BigDecimal parseDecimal(String input) {
        if (input == null || input.trim().isEmpty()) {
            return null;
        }
        
        try {
            return new BigDecimal(input.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    public static boolean isPositive(BigDecimal value) {
        return value != null && value.compareTo(BigDecimal.ZERO) > 0;
    }
    
    public static boolean isNonNegative(BigDecimal value) {
        return value != null && value.compareTo(BigDecimal.ZERO) >= 0;
    }
}