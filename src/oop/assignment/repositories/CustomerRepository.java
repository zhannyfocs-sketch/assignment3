package oop.assignment.repositories;

import oop.assignment.db.IDB;
import oop.assignment.entities.Customer;
import oop.assignment.repositories.interfaces.ICustomerRepository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerRepository implements ICustomerRepository {
    private final IDB db;

    public CustomerRepository(IDB db) {
        this.db = db;
    }

    @Override
    public List<Customer> findAll() throws SQLException {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers";

        try (Connection conn = db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Customer customer = new Customer.Builder()
                        .setId(rs.getInt("Id"))
                        .setFullName(rs.getString("full_name"))
                        .setEmail(rs.getString("email"))
                        .setDriverLicenseId(rs.getString("license_id"))
                        .setBirthdate(rs.getDate("birthdate").toLocalDate())
                        .build();
                customers.add(customer);
            }
        }
        return customers;
    }

    @Override
    public Customer findById(int id) throws SQLException {
        String sql = "SELECT * FROM customers WHERE id = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Customer.Builder()
                            .setId(rs.getInt("id"))
                            .setFullName(rs.getString("full_name"))
                            .setEmail(rs.getString("email"))
                            .setDriverLicenseId(rs.getString("license_id"))
                            .setBirthdate(rs.getDate("birthdate").toLocalDate())
                            .build();
                }
            }
        }
        return null;
    }

    @Override
    public void add(Customer c) throws SQLException {
        String sql = "INSERT INTO customers (full_name, email, license_id, birthdate) VALUES (?, ?, ?, ?)";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, c.getFullName());
            stmt.setString(2, c.getEmail());
            stmt.setString(3, c.getDriverLicenseId());
            stmt.setDate(4, Date.valueOf(c.getBirthdate()));
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) c.setId(rs.getInt(1));
            }
        }
    }
    @Override
    public Customer findByEmail(String email) throws SQLException {
        return null;
    }
}