package oop.assignment.BillingComponent;

public class TaxConfig {
    private static TaxConfig instance;
    private double taxRate = 0.12; // Business rule: Financial

    private TaxConfig() {}

    public static synchronized TaxConfig getInstance() {
        if (instance == null) instance = new TaxConfig();
        return instance;
    }
    public double getTaxRate() { return taxRate; }
}
