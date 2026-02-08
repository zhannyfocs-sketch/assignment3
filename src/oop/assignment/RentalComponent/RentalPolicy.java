package oop.assignment.RentalComponent;

public class RentalPolicy {
    private static RentalPolicy instance;
    private int minDriverAge = 18;

    private RentalPolicy() {}

    public static synchronized RentalPolicy getInstance() {
        if (instance == null) instance = new RentalPolicy();
        return instance;
    }
    public int getMinDriverAge() { return minDriverAge; }
}
