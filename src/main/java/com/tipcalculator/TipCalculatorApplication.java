package com.tipcalculator;

import com.tipcalculator.service.TipCalculationService;
import com.tipcalculator.ui.ConsoleInterface;

/**
 * Main application entry point for the Tip Calculator.
 */
public class TipCalculatorApplication {
    
    public static void main(String[] args) {
        TipCalculationService service = new TipCalculationService();
        ConsoleInterface ui = new ConsoleInterface(System.in, System.out, service);
        
        try {
            ui.run();
        } finally {
            ui.close();
        }
    }
}