package oop.assignment.BillingComponent;

import oop.assignment.shared.interfaces.IRepository;

import java.sql.SQLException;
import java.util.List;

public interface IPaymentRepository extends IRepository<Payment> {
    List<Payment> findByRentalId(int rentalId) throws SQLException;
}
