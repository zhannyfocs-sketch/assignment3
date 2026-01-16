package oop.assignment.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Rental {
    private int id;
    private int carId;
    private int customerId;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal totalCost;
    private String status;

    public Rental() {}

    public Rental(int carId, int customerId, LocalDate startDate, LocalDate endDate) {
        this.carId = carId;
        this.customerId = customerId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = "active";
    }

    public Rental(int id, int carId, int customerId, LocalDate startDate,
                  LocalDate endDate, BigDecimal totalCost, String status) {
        this.id = id;
        this.carId = carId;
        this.customerId = customerId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalCost = totalCost;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getRentalDays() {
        if (startDate == null || endDate == null)
            return 0;
        return java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate);
    }

    public boolean isValidDates() {
        if (startDate == null || endDate == null)
            return false;
        return !endDate.isBefore(startDate);
    }

    @Override
    public String toString() {
        long days = getRentalDays();
        return "Rental [id=" + id +
                ", carId=" + carId +
                ", customerId=" + customerId +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", totalCost=" + totalCost +
                ", status=" + status +
                ", rentalDays=" + days + "]";
    }
}