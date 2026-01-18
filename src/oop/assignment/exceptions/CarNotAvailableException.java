package oop.assignment.exceptions;

public class CarNotAvailableException extends Exception {
    public CarNotAvailableException(int carId) {
        super("The car with ID " + carId + " is currently unavailable for rent.");
    }
}