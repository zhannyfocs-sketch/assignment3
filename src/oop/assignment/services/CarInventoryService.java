package oop.assignment.services;

import oop.assignment.entities.Car;
import oop.assignment.exceptions.CarNotAvailableException;
import oop.assignment.factories.CarFactory;
import oop.assignment.repositories.interfaces.ICarRepository;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class CarInventoryService {
    private final ICarRepository carRepo;

    public CarInventoryService(ICarRepository carRepo) {
        this.carRepo = carRepo;
    }
    public Car getReadyCar(int id) throws SQLException, CarNotAvailableException {
        Car car = carRepo.findById(id);
        if (car == null || !car.isAvailable()) {
            throw new CarNotAvailableException(id);
        }
        return car;
    }

    public List<Car> getAllCars() throws SQLException {
        return carRepo.findAll();
    }
    public List<Car> getOnlyAvailableCars() throws SQLException {
        return carRepo.findAll().stream()
                .filter(Car::isAvailable)
                .collect(Collectors.toList());
    }
}