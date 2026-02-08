package oop.assignment.RentalComponent.exceptions;

public class InvalidDriverAgeException extends Exception {
    public InvalidDriverAgeException(int age) {
        super("The provided driver age (" + age + ") is invalid. Driver must be at least 18 years old.");
    }
}