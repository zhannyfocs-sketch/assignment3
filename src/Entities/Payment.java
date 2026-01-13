package Entities;

public class Payment {
    private int id;
    private static int idGen;
    private int rentalId;
    private double amount;

    public Payment( int rentalId, double amount) {
        id = idGen;
        idGen++;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
     public int getRentalId() {
        return rentalId;
     }
     public void setRentalId(int rentalId) {
        if (rentalId < 0 ) {
            throw new IllegalArgumentException("rentalId cannot be negative");
        }
        this.rentalId = rentalId;
     }
     public double getAmount() {
        return amount;
     }
     public void setAmount(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("amount cannot be negative");
        }
        this.amount = amount;
     }




}
