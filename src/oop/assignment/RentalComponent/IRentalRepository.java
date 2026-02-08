package oop.assignment.RentalComponent;

import oop.assignment.shared.interfaces.IRepository;

import java.sql.SQLException;
import java.time.LocalDate;

public interface IRentalRepository extends IRepository<Rental> {
    boolean hasOverlappingRental(int carId, LocalDate startDate, LocalDate endDate) throws SQLException;
    void updateStatus(int id, String status) throws SQLException;
}