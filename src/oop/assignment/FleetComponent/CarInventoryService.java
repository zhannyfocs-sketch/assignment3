package oop.assignment.FleetComponent;

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