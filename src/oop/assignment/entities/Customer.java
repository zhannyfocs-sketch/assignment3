package oop.assignment.entities;

import java.time.LocalDate;
import java.time.Period;

public class Customer {
    private int id;
    private String fullName;
    private String email;
    private String driverLicenseId;
    private LocalDate birthdate;

    private Customer(Builder builder) {
        this.id = builder.id;
        this.fullName = builder.fullName;
        this.email = builder.email;
        this.driverLicenseId = builder.driverLicenseId;
        this.birthdate = builder.birthdate;
    }

    public static class Builder {
        private int id;
        private String fullName;
        private String email;
        private String driverLicenseId;
        private LocalDate birthdate;

        public Builder setId(int id) { this.id = id; return this; }
        public Builder setFullName(String fullName) { this.fullName = fullName; return this; }
        public Builder setEmail(String email) { this.email = email; return this; }
        public Builder setDriverLicenseId(String driverLicenseId) { this.driverLicenseId = driverLicenseId; return this; }
        public Builder setBirthdate(LocalDate birthdate) { this.birthdate = birthdate; return this; }

        public Customer build() {
            return new Customer(this);
        }
    }
    public int getAge() {
        if (birthdate == null) return 0;
        return Period.between(birthdate, LocalDate.now()).getYears();
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getId() { return id; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getDriverLicenseId() { return driverLicenseId; }
    public LocalDate getBirthdate() { return birthdate; }

    @Override
    public String toString() {
        return "Customer [id=" + id + ", name=" + fullName + ", age=" + getAge() + "]";
    }
}