package oop.assignment.repositories.interfaces;

import oop.assignment.entities.Payment;

import java.sql.SQLException;
import java.util.List;

public interface IPaymentRepository extends IRepository<Payment>{
    List<Payment> findByRentalId(int rentalId) throws SQLException;
}
