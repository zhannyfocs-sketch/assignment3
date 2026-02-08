package oop.assignment.FleetComponent;

import oop.assignment.shared.interfaces.IRepository;

import java.sql.SQLException;
import java.util.List;

public interface ICarRepository extends IRepository<Car> {
    List<Car> findAvailable() throws SQLException;
    void updateAvailability(int id, boolean available) throws SQLException;
}