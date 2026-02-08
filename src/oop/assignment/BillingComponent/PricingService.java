package oop.assignment.BillingComponent;

import oop.assignment.FleetComponent.Car;
import oop.assignment.RentalComponent.Rental;
import oop.assignment.BillingComponent.exceptions.InvalidPaymentAmount;

import java.math.BigDecimal;

public class PricingService {

    public BigDecimal calculateTotal(Car car, Rental rental, boolean hadAccident) {
        long days = rental.getRentalDays();
        if (days <= 0) days = 1;
        InsurancePolicy policy = InsurancePolicy.getInstance();
        TaxConfig taxConfig = TaxConfig.getInstance();

        BigDecimal base = car.getRate().multiply(BigDecimal.valueOf(days));
        BigDecimal total = base.add(policy.getBaseInsuranceFee());

        BigDecimal taxRate = BigDecimal.valueOf(taxConfig.getTaxRate());
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