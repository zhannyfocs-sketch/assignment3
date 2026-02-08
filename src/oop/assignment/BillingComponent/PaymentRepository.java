package oop.assignment.BillingComponent;

import oop.assignment.db.IDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentRepository implements IPaymentRepository {
    private final IDB db;

    public PaymentRepository(IDB db) {
        this.db = db;
    }

    @Override
    public void add(Payment payment) throws SQLException {
        String sql = "INSERT INTO payments (rental_id, amount, payment_date, payment_method) VALUES (?, ?, ?, ?)";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, payment.getRentalId());
            stmt.setBigDecimal(2, payment.getAmount());
            stmt.setTimestamp(3, Timestamp.valueOf(payment.getPaymentDate()));
            stmt.setString(4, payment.getPaymentMethod());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    payment.setId(rs.getInt(1));
                }
            }
        }
    }
    @Override
    public Payment findById(int paymentId) throws SQLException {
        String sql = "SELECT * FROM payments WHERE id = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, paymentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Payment(
                            rs.getInt("id"),
                            rs.getInt("rental_id"),
                            rs.getBigDecimal("amount"),
                            rs.getTimestamp("payment_date").toLocalDateTime(),
                            rs.getString("payment_method")
                    );
                }
            }
        }
        return null;
    }
    @Override
    public List<Payment> findAll() throws SQLException {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT * FROM payments";
        try (Connection conn = db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                payments.add(new Payment(
                        rs.getInt("id"),
                        rs.getInt("rental_id"),
                        rs.getBigDecimal("amount"),
                        rs.getTimestamp("payment_date").toLocalDateTime(),
                        rs.getString("payment_method")
                ));
            }
        }
        return payments;
    }
    @Override
    public List<Payment> findByRentalId(int rentalId) throws SQLException {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT * FROM payments WHERE rental_id = ?";

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, rentalId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    payments.add(new Payment(
                            rs.getInt("id"),
                            rs.getInt("rental_id"),
                            rs.getBigDecimal("amount"),
                            rs.getTimestamp("payment_date").toLocalDateTime(),
                            rs.getString("payment_method")
                    ));
                }
            }
        }
        return payments;
    }
}