package com.tipcalculator.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Immutable value object representing a bill with tip calculation.
 * Uses BigDecimal for precise monetary calculations.
 */
public final class Bill {
    private static final int SCALE = 2;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;
    
    private final BigDecimal subtotal;
    private final BigDecimal tipPercentage;
    private final BigDecimal tipAmount;
    private final BigDecimal total;
    
    /**
     * Creates a new Bill with calculated tip and total.
     */
    public Bill(BigDecimal subtotal, BigDecimal tipPercentage) {
        validateInputs(subtotal, tipPercentage);
        
        this.subtotal = subtotal.setScale(SCALE, ROUNDING_MODE);
        this.tipPercentage = tipPercentage.setScale(SCALE, ROUNDING_MODE);
        this.tipAmount = calculateTipAmount(this.subtotal, this.tipPercentage);
        this.total = this.subtotal.add(this.tipAmount);
    }
    
    private void validateInputs(BigDecimal subtotal, BigDecimal tipPercentage) {
        if (subtotal == null || tipPercentage == null) {
            throw new IllegalArgumentException("Subtotal and tip percentage cannot be null");
        }
        if (subtotal.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Subtotal cannot be negative");
        }
        if (tipPercentage.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Tip percentage cannot be negative");
        }
    }
    
    private BigDecimal calculateTipAmount(BigDecimal subtotal, BigDecimal tipPercentage) {
        return subtotal
            .multiply(tipPercentage)
            .divide(new BigDecimal("100"), SCALE, ROUNDING_MODE);
    }
    
    public BigDecimal getSubtotal() {
        return subtotal;
    }
    
    public BigDecimal getTipPercentage() {
        return tipPercentage;
    }
    
    public BigDecimal getTipAmount() {
        return tipAmount;
    }
    
    public BigDecimal getTotal() {
        return total;
    }
    
    public boolean isGenerousTip() {
        return tipPercentage.compareTo(new BigDecimal("25")) > 0;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bill bill = (Bill) o;
        return Objects.equals(subtotal, bill.subtotal) &&
               Objects.equals(tipPercentage, bill.tipPercentage);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(subtotal, tipPercentage);
    }
    
    @Override
    public String toString() {
        return String.format("Bill{subtotal=%s, tip=%s%%, tipAmount=%s, total=%s}",
            subtotal, tipPercentage, tipAmount, total);
    }
}