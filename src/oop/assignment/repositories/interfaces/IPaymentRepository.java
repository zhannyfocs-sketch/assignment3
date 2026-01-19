package oop.assignment.repositories.interfaces;

import oop.assignment.entities.Payment;

import java.sql.SQLException;
import java.util.List;

public interface IPaymentRepository {
    void add(Payment payment) throws SQLException;
    Payment findByID(int paymentId) throws SQLException;
    List<Payment> getAllPayments() throws SQLException;
}
