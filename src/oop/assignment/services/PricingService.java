package oop.assignment.services;

import oop.assignment.singletones.FleetConfig;
import oop.assignment.singletones.InsurancePolicy;
import oop.assignment.entities.Car;
import oop.assignment.entities.Rental;
import oop.assignment.exceptions.InvalidPaymentAmount;

import java.math.BigDecimal;

public class PricingService {

    public BigDecimal calculateTotal(Car car, Rental rental, boolean hadAccident) {
        long days = rental.getRentalDays();
        if (days <= 0) days = 1;
        InsurancePolicy policy = InsurancePolicy.getInstance();
        FleetConfig fleetConfig = FleetConfig.getInstance();

        BigDecimal base = car.getRate().multiply(BigDecimal.valueOf(days));
        BigDecimal total = base.add(policy.getBaseInsuranceFee());

        BigDecimal taxRate = BigDecimal.valueOf(fleetConfig.getTaxRate());
        BigDecimal taxAmount = total.multiply(taxRate);
        BigDecimal totalWithTax = total.add(taxAmount);

        if (hadAccident) {
            totalWithTax = totalWithTax.add(policy.getStandardAccidentFee());
        }
        if (total.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidPaymentAmount("Payment must be a positive value. Calculated: " + total);
        }

        return totalWithTax;
    }
}