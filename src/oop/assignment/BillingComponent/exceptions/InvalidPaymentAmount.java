package oop.assignment.BillingComponent.exceptions;

public class InvalidPaymentAmount extends RuntimeException {
    public InvalidPaymentAmount(String message) {
        super(message);
    }
}
