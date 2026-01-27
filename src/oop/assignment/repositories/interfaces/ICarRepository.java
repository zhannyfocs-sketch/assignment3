package oop.assignment.repositories.interfaces;

import oop.assignment.entities.Car;
import java.sql.SQLException;
import java.util.List;

public interface ICarRepository extends IRepository<Car> {
    List<Car> findAvailable() throws SQLException;
    void updateAvailability(int id, boolean available) throws SQLException;
}