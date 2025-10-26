package com.tipcalculator.ui;

import com.tipcalculator.model.Bill;
import com.tipcalculator.service.TipCalculationService;
import com.tipcalculator.util.InputValidator;

import java.io.InputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.Scanner;

/**
 * Console-based user interface for the tip calculator.
 */
public class ConsoleInterface {
    
    private final Scanner scanner;
    private final PrintStream output;
    private final TipCalculationService service;
    
    public ConsoleInterface(InputStream input, PrintStream output, TipCalculationService service) {
        this.scanner = new Scanner(input);
        this.output = output;
        this.service = service;
    }
    
    public void run() {
        printWelcome();
        
        BigDecimal subtotal = promptForSubtotal();
        BigDecimal tipPercentage = promptForTipPercentage();
        
        Bill bill = service.createBill(subtotal, tipPercentage);
        output.printf("Tip amount: %s\n", service.formatMoney(bill.getTipAmount()));
        
        output.println(service.generateBillBreakdown(bill));
    }
    
    private void printWelcome() {
        output.println("\n" + "=".repeat(50));
        output.println("💰 TIP CALCULATOR");
        output.println("=".repeat(50) + "\n");
    }
    
    private BigDecimal promptForSubtotal() {
        while (true) {
            output.print("Enter total bill amount: $");
            String input = scanner.nextLine();
            
            BigDecimal value = InputValidator.parseDecimal(input);
            
            if (!InputValidator.isPositive(value)) {
                output.println("Please enter a valid positive amount.");
                continue;
            }
            
            return value;
        }
    }
    
    private BigDecimal promptForTipPercentage() {
        while (true) {
            output.print("Enter tip percentage (e.g., 15 for 15%): ");
            String input = scanner.nextLine();
            
            BigDecimal value = InputValidator.parseDecimal(input);
            
            if (!InputValidator.isNonNegative(value)) {
                output.println("Please enter a valid non-negative percentage.");
                continue;
            }
            
            return value;
        }
    }
    
    public void close() {
        scanner.close();
    }
}