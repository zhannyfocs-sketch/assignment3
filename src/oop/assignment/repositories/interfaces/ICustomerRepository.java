package oop.assignment.repositories.interfaces;

import oop.assignment.entities.Customer;
import java.sql.SQLException;
import java.util.List;

public interface ICustomerRepository {
    void add(Customer customer) throws SQLException;
    Customer findById(int id) throws SQLException;
    Customer findByEmail(String email) throws SQLException;
    List<Customer> findAll() throws SQLException;
}