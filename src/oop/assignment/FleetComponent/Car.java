package oop.assignment.FleetComponent;

import java.math.BigDecimal;

public abstract class Car {
    private int id;
    private String make;
    private String model;
    private BigDecimal rate;
    private boolean available;

    public Car() {}

    protected Car(String make, String model, BigDecimal rate, boolean available) {
        setMake(make);
        setModel(model);
        setRate(rate);
        setAvailable(available);
    }
    public Car(int id, String make, String model, BigDecimal rate, boolean available) {
        this(make, model, rate, available);
        setId(id);
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMake() {
        return make;
    }
    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }
    public void setModel(String model) {
        this.model = model;
    }

    public BigDecimal getRate() {
        return rate;
    }
    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
    public String getType() {
        return this.getClass().getSimpleName().replace("Car", "");
    }

    public boolean isAvailable() {
        return available;
    }
    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return "Car{" + "id=" + id + ", make=" + make
                + ", model=" + model + ", rate=" + rate + ", available=" + available + '}';
    }
}