package oop.assignment.RentalComponent;

import oop.assignment.db.IDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RentalRepository implements IRentalRepository {
    private final IDB db;

    public RentalRepository(IDB db) {
        this.db = db;
    }

    @Override
    public void add(Rental rental) throws SQLException {
        String sql = "INSERT INTO rentals (car_id, customer_id, start_date, end_date, total_cost, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, rental.getCarId());
            stmt.setInt(2, rental.getCustomerId());
            stmt.setDate(3, Date.valueOf(rental.getStartDate()));
            stmt.setDate(4, Date.valueOf(rental.getEndDate()));
            stmt.setBigDecimal(5, rental.getTotalCost());
            stmt.setString(6, rental.getStatus());

            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) rental.setId(rs.getInt(1));
            }
        }
    }

    @Override
    public List<Rental> findAll() throws SQLException {
        List<Rental> rentals = new ArrayList<>();
        String sql = "SELECT * FROM rentals";

        try (Connection conn = db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Rental rental = new Rental.Builder()
                        .setId(rs.getInt("id"))
                        .setCarId(rs.getInt("car_id"))
                        .setCustomerId(rs.getInt("customer_id"))
                        .setStartDate(rs.getDate("start_date").toLocalDate())
                        .setEndDate(rs.getDate("end_date").toLocalDate())
                        .setTotalCost(rs.getBigDecimal("total_cost"))
                        .setStatus(rs.getString("status"))
                        .build();
                rentals.add(rental);
            }
        }
        return rentals;
    }

    @Override
    public Rental findById(int id) throws SQLException {
        String sql = "SELECT * FROM rentals WHERE id = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Rental.Builder()
                            .setId(rs.getInt("id"))
                            .setCarId(rs.getInt("car_id"))
                            .setCustomerId(rs.getInt("customer_id"))
                            .setStartDate(rs.getDate("start_date").toLocalDate())
                            .setEndDate(rs.getDate("end_date").toLocalDate())
                            .setTotalCost(rs.getBigDecimal("total_cost"))
                            .setStatus(rs.getString("status"))
                            .build();
                }
            }
        }
        return null;
    }

    @Override
    public boolean hasOverlappingRental(int carId, java.time.LocalDate start, java.time.LocalDate end) throws SQLException {
        String sql = "SELECT COUNT(*) FROM rentals WHERE car_id = ? AND status = 'active' " +
                "AND (start_date < ? AND end_date > ?)";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, carId);
            stmt.setDate(2, Date.valueOf(end));
            stmt.setDate(3, Date.valueOf(start));
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    @Override
    public void updateStatus(int id, String status) throws SQLException {
        String sql = "UPDATE rentals SET status = ? WHERE id = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        }
    }
}