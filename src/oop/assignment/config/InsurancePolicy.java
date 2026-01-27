package oop.assignment.config;

import java.math.BigDecimal;

public class InsurancePolicy {
    private static InsurancePolicy instance;
    private final BigDecimal baseInsuranceFee = new BigDecimal("20000");
    private final BigDecimal standardAccidentFee = new BigDecimal("50000");

    private InsurancePolicy() {}

    public static synchronized InsurancePolicy getInstance() {
        if (instance == null) {
            instance = new InsurancePolicy();
        }
        return instance;
    }

    public BigDecimal getBaseInsuranceFee() { return baseInsuranceFee; }
    public BigDecimal getStandardAccidentFee() { return standardAccidentFee; }
}