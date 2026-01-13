package Entities;

public class Rental {
    private int id;
    private static int idGen;
    private int customerId;
    private int carId;
    private java.sql.Date startdate;
    private java.sql.Date enddate;
    private double totalCost;
    private String status;

    public Rental(int customerId, int carId, java.sql.Date startdate, java.sql.Date enddate, double totalCost, String status) {
        id = idGen;
        idGen++;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        if (customerId <= 0) {
            throw new IllegalArgumentException("Invalid Customer ID");
        }
        this.customerId = customerId;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        if (carId <= 0) {
            throw new IllegalArgumentException("Invalid Car ID");
        }
        this.carId = carId;
    }

    public java.sql.Date getStartdate() {
        return startdate;
    }

    public void setStartdate(java.sql.Date startdate) {
        if (startdate == null) {
            throw new IllegalArgumentException("Start date cannot be null");
        }
        this.startdate = startdate;
    }

    public java.sql.Date getEnddate() {
        return enddate;
    }

    public void setEnddate(java.sql.Date enddate) {
        if (enddate == null || enddate.before(startdate)) {
            throw new IllegalArgumentException("End date cannot be null");
        }
        this.enddate = enddate;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        if (totalCost < 0) {
            throw new IllegalArgumentException("Total cost cannot be negative");
        }
        this.totalCost = totalCost;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (status == null || status.isEmpty()) {
            throw new IllegalArgumentException("Status cannot be blank");
        }
        this.status = status;
    }

    @Override
    public String toString() {
        return "Rental{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", carId=" + carId +
                ", startdate=" + startdate +
                ", enddate=" + enddate +
                ", totalCost=" + totalCost +
                ", status='" + status + '\'' +
                '}';
    }

}


