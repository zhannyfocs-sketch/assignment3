package oop.assignment.entities.cartypes;

import oop.assignment.entities.Car;
import java.math.BigDecimal;

public class SUV extends Car {
    public SUV(String make, String model, BigDecimal rate,  boolean available) {
        super(make, model, rate, available);
    }
}
