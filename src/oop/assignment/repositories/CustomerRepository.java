package oop.assignment.repositories;

import oop.assignment.db.IDB;
import oop.assignment.entities.Customer;
import oop.assignment.repositories.interfaces.ICustomerRepository;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CustomerRepository implements ICustomerRepository {
    private final IDB db;

    public CustomerRepository(IDB db) {
        this.db = db;
    }

    @Override
    public void add(Customer customer) throws SQLException {
        String sql = "INSERT INTO customers (fullName, email, driverLicenseId, birthdate) VALUES (?, ?, ?, ?)";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, customer.getFullName());
            stmt.setString(2, customer.getEmail());
            stmt.setString(3, customer.getDriverLicenseId());
            stmt.setDate(4, Date.valueOf(customer.getBirthdate()));
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) customer.setId(rs.getInt(1));
            }
        }
    }

    @Override
    public Customer findById(int id) throws SQLException {
        String sql = "SELECT * FROM customers WHERE id = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Customer(
                            rs.getInt("id"),
                            rs.getString("fullName"),
                            rs.getString("email"),
                            rs.getString("driverLicenseId"),
                            rs.getDate("birthdate").toLocalDate()
                    );
                }
            }
        }
        return null;
    }

    @Override
    public Customer findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM customers WHERE email = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Customer(
                            rs.getInt("id"),
                            rs.getString("fullName"),
                            rs.getString("email"),
                            rs.getString("driverLicenseId"),
                            rs.getDate("birthdate").toLocalDate()
                    );
                }
            }
        }
        return null;
    }

    @Override
    public List<Customer> findAll() throws SQLException {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers";
        try (Connection conn = db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                customers.add(new Customer(
                        rs.getInt("id"),
                        rs.getString("fullName"),
                        rs.getString("email"),
                        rs.getString("driverLicenseId"),
                        rs.getDate("birthdate").toLocalDate()
                ));
            }
        }
        return customers;
    }
}