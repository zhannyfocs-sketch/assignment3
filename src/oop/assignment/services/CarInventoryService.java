package oop.assignment.services;

import oop.assignment.entities.Car;
import oop.assignment.repositories.interfaces.ICarRepository;

import java.sql.SQLException;
import java.util.List;

public class CarInventoryService {
    private final ICarRepository carRepo;

    public CarInventoryService(ICarRepository carRepo) {
        this.carRepo = carRepo;
    }
    public List<Car> getOnlyAvailableCars() throws SQLException {
        return carRepo.findAvailable();
    }
}