package oop.assignment.factories;

import oop.assignment.entities.*;
import oop.assignment.entities.cartypes.*;
import java.math.BigDecimal;

public class CarFactory {
    public static Car createCar(String type, String make, String model, BigDecimal rate, boolean available) {
        if (type == null) return null;
        return switch (type.toUpperCase()) {
            case "SUV" -> new SUV(make, model, rate, available);
            case "ELECTRIC" -> new ElectricCar(make, model, rate, available);
            default -> new EconomyCar(make, model, rate, available);
        };
    }
}