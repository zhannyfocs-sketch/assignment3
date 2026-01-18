package oop.assignment.repositories;

import oop.assignment.db.IDB;
import oop.assignment.entities.Rental;
import oop.assignment.repositories.interfaces.IRentalRepository;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RentalRepository implements IRentalRepository {
    private final IDB db;

    public RentalRepository(IDB db) {
        this.db = db;
    }

    @Override
    public void add(Rental rental) throws SQLException {
        String sql = "INSERT INTO rentals (car_id, customer_id, start_date, end_date, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, rental.getCarId());
            stmt.setInt(2, rental.getCustomerId());
            stmt.setDate(3, Date.valueOf(rental.getStartDate()));
            stmt.setDate(4, Date.valueOf(rental.getEndDate()));
            stmt.setString(5, rental.getStatus() != null ? rental.getStatus() : "active");
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) rental.setId(rs.getInt(1));
            }
        }
    }

    @Override
    public Rental findById(int id) throws SQLException {
        String sql = "SELECT * FROM rentals WHERE id = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Rental(
                            rs.getInt("id"),
                            rs.getInt("car_id"),
                            rs.getInt("customer_id"),
                            rs.getDate("start_date").toLocalDate(),
                            rs.getDate("end_date").toLocalDate(),
                            rs.getBigDecimal("total_cost"),
                            rs.getString("status")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public List<Rental> findAll() throws SQLException {
        List<Rental> rentals = new ArrayList<>();
        String sql = "SELECT * FROM rentals";
        try (Connection conn = db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                rentals.add(new Rental(
                        rs.getInt("id"),
                        rs.getInt("car_id"),
                        rs.getInt("customer_id"),
                        rs.getDate("start_date").toLocalDate(),
                        rs.getDate("end_date").toLocalDate(),
                        rs.getBigDecimal("total_cost"),
                        rs.getString("status")
                ));
            }
        }
        return rentals;
    }

    @Override
    public boolean hasOverlappingRental(int carId, LocalDate startDate, LocalDate endDate) throws SQLException {
        String sql = "SELECT COUNT(*) FROM rentals WHERE car_id = ? AND end_date >= ? AND start_date <= ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, carId);
            stmt.setDate(2, Date.valueOf(startDate));
            stmt.setDate(3, Date.valueOf(endDate));
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}