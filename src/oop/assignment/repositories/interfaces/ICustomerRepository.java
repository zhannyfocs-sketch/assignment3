package oop.assignment.repositories.interfaces;

import oop.assignment.entities.Customer;
import java.sql.SQLException;

public interface ICustomerRepository extends IRepository<Customer> {
    Customer findByEmail(String email) throws SQLException;
}