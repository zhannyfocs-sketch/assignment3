package Entities;

public class Customer {
    private int id;
    private String fullName;
    private String email;
    private String driverLicenseId;

    public Customer(String fullName, String email, String driverLicenseId) {
        setFullName(fullName);
        setEmail(email);
        setDriverLicenseId(driverLicenseId);
    }
    public Customer(int id, String fullName, String email, String driverLicenseId) {
        this.id = id;
        setFullName(fullName);
        setEmail(email);
        setDriverLicenseId(driverLicenseId);
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        if (fullName == null) {
            throw  new IllegalArgumentException("fullName cannot be empty");
        }
        this.fullName = fullName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        if (email == null) {
            throw  new IllegalArgumentException("email cannot be empty");
        }
        this.email = email;
    }
    public String getDriverLicenseId() {
        return driverLicenseId;
    }
    public void setDriverLicenseId(String driverLicenseId) {
        if (driverLicenseId == null) {
            throw  new IllegalArgumentException("driverLicenseId cannot be empty");
        }
        this.driverLicenseId = driverLicenseId;
    }

    @Override
    public String toString() {
        return "id"+id+"fullName"+fullName+"email"+email+"driverLicenseId"+driverLicenseId;
    }
}
