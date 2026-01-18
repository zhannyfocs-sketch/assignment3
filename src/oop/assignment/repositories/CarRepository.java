package oop.assignment.repositories;

import oop.assignment.db.IDB;
import oop.assignment.entities.Car;
import oop.assignment.repositories.interfaces.ICarRepository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarRepository implements ICarRepository {
    private final IDB db;

    public CarRepository(IDB db) {
        this.db = db;
    }

    @Override
    public void add(Car car) throws SQLException {
        String sql = "INSERT INTO cars (make, model, rate, available) VALUES (?, ?, ?, ?)";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, car.getMake());
            stmt.setString(2, car.getModel());
            stmt.setBigDecimal(3, car.getRate());
            stmt.setBoolean(4, car.isAvailable());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) car.setId(rs.getInt(1));
            }
        }
    }

    @Override
    public Car findById(int id) throws SQLException {
        String sql = "SELECT * FROM cars WHERE id = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Car(
                            rs.getInt("id"),
                            rs.getString("make"),
                            rs.getString("model"),
                            rs.getBigDecimal("rate"),
                            rs.getBoolean("available")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public List<Car> findAll() throws SQLException {
        List<Car> cars = new ArrayList<>();
        String sql = "SELECT * FROM cars";
        try (Connection conn = db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                cars.add(new Car(
                        rs.getInt("id"),
                        rs.getString("make"),
                        rs.getString("model"),
                        rs.getBigDecimal("rate"),
                        rs.getBoolean("available")
                ));
            }
        }
        return cars;
    }

    @Override
    public List<Car> findAvailable() throws SQLException {
        List<Car> cars = new ArrayList<>();
        String sql = "SELECT * FROM cars WHERE available = true";
        try (Connection conn = db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                cars.add(new Car(
                        rs.getInt("id"),
                        rs.getString("make"),
                        rs.getString("model"),
                        rs.getBigDecimal("rate"),
                        rs.getBoolean("available")
                ));
            }
        }
        return cars;
    }
}