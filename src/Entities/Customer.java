package Entities;

public class Customer {
    private int id;
    private String fullname;
    private String email;
    private String driverlicenseid;

    public Customer(String fullname, String email, String driverlicenseid) {
        this.fullname = fullname;
        this.email = email;
        this.driverlicenseid = driverlicenseid;
    }
}
