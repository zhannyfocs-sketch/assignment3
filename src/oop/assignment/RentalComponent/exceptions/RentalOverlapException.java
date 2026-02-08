package oop.assignment.RentalComponent.exceptions;

import java.time.LocalDate;

public class RentalOverlapException extends Exception {
    public RentalOverlapException(int carId, LocalDate startDate, LocalDate endDate) {
        super("The car with ID " + carId + " is already booked between " + startDate + " and " + endDate + ".");
    }
}