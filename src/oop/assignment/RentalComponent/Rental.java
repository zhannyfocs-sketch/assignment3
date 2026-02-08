package oop.assignment.RentalComponent;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Rental {
    private int id;
    private int carId;
    private int customerId;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal totalCost;
    private String status;

    private Rental(Builder builder) {
        this.id = builder.id;
        this.carId = builder.carId;
        this.customerId = builder.customerId;
        this.startDate = builder.startDate;
        this.endDate = builder.endDate;
        this.totalCost = builder.totalCost;
        this.status = builder.status;
    }

    public static class Builder {
        private int id;
        private int carId;
        private int customerId;
        private LocalDate startDate;
        private LocalDate endDate;
        private BigDecimal totalCost;
        private String status = "active"; // Default value

        public Builder setId(int id) { this.id = id; return this; }
        public Builder setCarId(int carId) { this.carId = carId; return this; }
        public Builder setCustomerId(int customerId) { this.customerId = customerId; return this; }
        public Builder setStartDate(LocalDate startDate) { this.startDate = startDate; return this; }
        public Builder setEndDate(LocalDate endDate) { this.endDate = endDate; return this; }
        public Builder setTotalCost(BigDecimal totalCost) { this.totalCost = totalCost; return this; }
        public Builder setStatus(String status) { this.status = status; return this; }

        public Rental build() {
            return new Rental(this);
        }
    }
    public void setId(int id) {
        this.id = id;
    }
    public long getRentalDays() {
        if (startDate == null || endDate == null) return 0;
        return ChronoUnit.DAYS.between(startDate, endDate);
    }
    public int getId() { return id; }
    public int getCarId() { return carId; }
    public int getCustomerId() { return customerId; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public BigDecimal getTotalCost() { return totalCost; }
    public String getStatus() { return status; }

    @Override
    public String toString() {
        return "Rental [id=" + id + ", carId=" + carId + ", customerId=" + customerId +
                ", status=" + status + ", days=" + getRentalDays() + "]";
    }
}