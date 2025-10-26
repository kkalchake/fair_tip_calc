package com.tipcalculator.service;

import com.tipcalculator.model.Bill;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TipCalculationService Tests")
class TipCalculationServiceTest {
    
    private TipCalculationService service;
    
    @BeforeEach
    void setUp() {
        service = new TipCalculationService();
    }
    
    @Test
    @DisplayName("Should create bill with correct tip calculation")
    void testCreateBill() {
        BigDecimal subtotal = new BigDecimal("100.00");
        BigDecimal tipPercentage = new BigDecimal("15");
        
        Bill bill = service.createBill(subtotal, tipPercentage);
        
        assertNotNull(bill);
        assertEquals(new BigDecimal("100.00"), bill.getSubtotal());
        assertEquals(new BigDecimal("15.00"), bill.getTipPercentage());
        assertEquals(new BigDecimal("15.00"), bill.getTipAmount());
        assertEquals(new BigDecimal("115.00"), bill.getTotal());
    }
    
    @Test
    @DisplayName("Should identify generous tip above 25%")
    void testIsGenerousTip_Generous() {
        BigDecimal generousTip = new BigDecimal("30");
        assertTrue(service.isGenerousTip(generousTip));
    }
    
    @Test
    @DisplayName("Should identify non-generous tip at or below 25%")
    void testIsGenerousTip_NotGenerous() {
        BigDecimal normalTip = new BigDecimal("20");
        BigDecimal exactThreshold = new BigDecimal("25");
        
        assertFalse(service.isGenerousTip(normalTip));
        assertFalse(service.isGenerousTip(exactThreshold));
    }
    
    @Test
    @DisplayName("Should format money with dollar sign and two decimals")
    void testFormatMoney() {
        BigDecimal amount = new BigDecimal("123.456");
        String formatted = service.formatMoney(amount);
        assertEquals("$123.46", formatted);
    }
    
    @Test
    @DisplayName("Should generate bill breakdown without generosity message")
    void testGenerateBillBreakdown_Normal() {
        Bill bill = new Bill(new BigDecimal("50.00"), new BigDecimal("15"));
        String breakdown = service.generateBillBreakdown(bill);
        
        assertNotNull(breakdown);
        assertTrue(breakdown.contains("$50.00"));
        assertTrue(breakdown.contains("15.00%"));
        assertTrue(breakdown.contains("$7.50"));
        assertTrue(breakdown.contains("$57.50"));
        assertFalse(breakdown.contains("generosity"));
    }
    
    @Test
    @DisplayName("Should generate bill breakdown with generosity message for tips > 25%")
    void testGenerateBillBreakdown_Generous() {
        Bill bill = new Bill(new BigDecimal("100.00"), new BigDecimal("30"));
        String breakdown = service.generateBillBreakdown(bill);
        
        assertNotNull(breakdown);
        assertTrue(breakdown.contains("Thank you for your generosity"));
    }
}