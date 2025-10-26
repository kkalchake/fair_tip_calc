package com.tipcalculator.service;

import com.tipcalculator.model.Bill;
import java.math.BigDecimal;

/**
 * Service layer for tip calculations.
 */
public class TipCalculationService {
    
    private static final BigDecimal GENEROUS_TIP_THRESHOLD = new BigDecimal("25");
    
    public Bill createBill(BigDecimal subtotal, BigDecimal tipPercentage) {
        return new Bill(subtotal, tipPercentage);
    }
    
    public boolean isGenerousTip(BigDecimal tipPercentage) {
        return tipPercentage.compareTo(GENEROUS_TIP_THRESHOLD) > 0;
    }
    
    public String formatMoney(BigDecimal amount) {
        return String.format("$%.2f", amount);
    }
    
    public String generateBillBreakdown(Bill bill) {
        StringBuilder breakdown = new StringBuilder();
        breakdown.append("\n").append("=".repeat(50)).append("\n");
        breakdown.append("BILL BREAKDOWN\n");
        breakdown.append("=".repeat(50)).append("\n");
        breakdown.append(String.format("Subtotal:        %s\n", formatMoney(bill.getSubtotal())));
        breakdown.append(String.format("Tip (%.2f%%):     %s\n", 
            bill.getTipPercentage(), formatMoney(bill.getTipAmount())));
        breakdown.append("-".repeat(50)).append("\n");
        breakdown.append(String.format("Total:           %s\n", formatMoney(bill.getTotal())));
        breakdown.append("=".repeat(50)).append("\n");
        
        if (bill.isGenerousTip()) {
            breakdown.append("\n🌟 Thank you for your generosity! 🌟\n");
        }
        
        return breakdown.toString();
    }
}