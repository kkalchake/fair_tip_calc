package com.tipcalculator.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Bill Model Tests")
class BillTest {
    
    @Test
    @DisplayName("Should create bill with correct calculations")
    void testBillCreation() {
        BigDecimal subtotal = new BigDecimal("100");
        BigDecimal tipPercentage = new BigDecimal("18");
        
        Bill bill = new Bill(subtotal, tipPercentage);
        
        assertEquals(new BigDecimal("100.00"), bill.getSubtotal());
        assertEquals(new BigDecimal("18.00"), bill.getTipPercentage());
        assertEquals(new BigDecimal("18.00"), bill.getTipAmount());
        assertEquals(new BigDecimal("118.00"), bill.getTotal());
    }
    
    @Test
    @DisplayName("Should round tip amount correctly")
    void testTipRounding() {
        BigDecimal subtotal = new BigDecimal("33.33");
        BigDecimal tipPercentage = new BigDecimal("15");
        
        Bill bill = new Bill(subtotal, tipPercentage);
        
        assertEquals(new BigDecimal("5.00"), bill.getTipAmount());
    }
    
    @Test
    @DisplayName("Should handle zero tip")
    void testZeroTip() {
        BigDecimal subtotal = new BigDecimal("50.00");
        BigDecimal tipPercentage = BigDecimal.ZERO;
        
        Bill bill = new Bill(subtotal, tipPercentage);
        
        assertEquals(BigDecimal.ZERO.setScale(2), bill.getTipAmount());
        assertEquals(subtotal, bill.getTotal());
    }
    
    @Test
    @DisplayName("Should identify generous tip correctly")
    void testIsGenerousTip() {
        Bill generousBill = new Bill(new BigDecimal("100"), new BigDecimal("30"));
        Bill normalBill = new Bill(new BigDecimal("100"), new BigDecimal("15"));
        
        assertTrue(generousBill.isGenerousTip());
        assertFalse(normalBill.isGenerousTip());
    }
    
    @Test
    @DisplayName("Should throw exception for null subtotal")
    void testNullSubtotal() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Bill(null, new BigDecimal("15"));
        });
    }
    
    @Test
    @DisplayName("Should throw exception for null tip percentage")
    void testNullTipPercentage() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Bill(new BigDecimal("100"), null);
        });
    }
    
    @Test
    @DisplayName("Should throw exception for negative subtotal")
    void testNegativeSubtotal() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Bill(new BigDecimal("-50"), new BigDecimal("15"));
        });
    }
    
    @Test
    @DisplayName("Should throw exception for negative tip percentage")
    void testNegativeTipPercentage() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Bill(new BigDecimal("100"), new BigDecimal("-15"));
        });
    }
    
    @Test
    @DisplayName("Should implement equals correctly")
    void testEquals() {
        Bill bill1 = new Bill(new BigDecimal("100"), new BigDecimal("15"));
        Bill bill2 = new Bill(new BigDecimal("100"), new BigDecimal("15"));
        Bill bill3 = new Bill(new BigDecimal("100"), new BigDecimal("20"));
        
        assertEquals(bill1, bill2);
        assertNotEquals(bill1, bill3);
    }
    
    @Test
    @DisplayName("Should implement hashCode correctly")
    void testHashCode() {
        Bill bill1 = new Bill(new BigDecimal("100"), new BigDecimal("15"));
        Bill bill2 = new Bill(new BigDecimal("100"), new BigDecimal("15"));
        
        assertEquals(bill1.hashCode(), bill2.hashCode());
    }
}