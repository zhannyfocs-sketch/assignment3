package oop.assignment.config;

public class FleetConfig {
    private static FleetConfig instance;

    private int minDriverAge = 18;
    private double taxRate = 0.12;

    private FleetConfig() {}

    public static synchronized FleetConfig getInstance() {
        if (instance == null) {
            instance = new FleetConfig();
        }
        return instance;
    }

    public int getMinDriverAge() { return minDriverAge; }
    public double getTaxRate() { return taxRate; }
}