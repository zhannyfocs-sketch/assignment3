package oop.assignment.repositories.interfaces;

import oop.assignment.entities.Car;
import java.sql.SQLException;
import java.util.List;

public interface ICarRepository {
    void add(Car car) throws SQLException;
    Car findById(int id) throws SQLException;
    List<Car> findAll() throws SQLException;
    List<Car> findAvailable() throws SQLException;
}