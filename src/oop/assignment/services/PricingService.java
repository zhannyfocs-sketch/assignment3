package oop.assignment.services;

import oop.assignment.config.InsurancePolicy;
import oop.assignment.entities.Car;
import oop.assignment.entities.Rental;
import oop.assignment.exceptions.InvalidPaymentAmount;

import java.math.BigDecimal;

public class PricingService {

    public BigDecimal calculateTotal(Car car, Rental rental, boolean hadAccident) {
        long days = rental.getRentalDays();
        if (days <= 0) days = 1;
        InsurancePolicy policy = InsurancePolicy.getInstance();

        BigDecimal base = car.getRate().multiply(BigDecimal.valueOf(days));
        BigDecimal total = base.add(policy.getBaseInsuranceFee());

        if (hadAccident) {
            total = total.add(policy.getStandardAccidentFee());
        }
        if (total.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidPaymentAmount("Payment must be a positive value. Calculated: " + total);
        }

        return total;
    }
}