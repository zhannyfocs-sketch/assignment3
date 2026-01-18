package oop.assignment.repositories.interfaces;

import oop.assignment.entities.Rental;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface IRentalRepository {
    void add(Rental rental) throws SQLException;
    Rental findById(int id) throws SQLException;
    List<Rental> findAll() throws SQLException;
    boolean hasOverlappingRental(int carId, LocalDate startDate, LocalDate endDate) throws SQLException;
}