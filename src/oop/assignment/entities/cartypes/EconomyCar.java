package oop.assignment.entities.cartypes;

import oop.assignment.entities.Car;
import java.math.BigDecimal;

public class EconomyCar extends Car {
    public EconomyCar(String make, String model, BigDecimal price, boolean available) {
        super(make, model, price, available);
    }
}
