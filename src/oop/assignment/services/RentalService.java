package oop.assignment.services;

import oop.assignment.entities.*;
import oop.assignment.exceptions.*;
import oop.assignment.repositories.interfaces.*;

import java.sql.SQLException;
import java.math.BigDecimal;
import java.util.List;

public class RentalService {
    private final IRentalRepository rentalRepo;
    private final ICarRepository carRepo;
    private final ICustomerRepository customerRepo;
    private final IPaymentRepository paymentRepo;
    private final PricingService pricingService;
    public RentalService(IRentalRepository rentalRepo,
                         ICarRepository carRepo,
                         ICustomerRepository customerRepo,
                         IPaymentRepository paymentRepo,
                         PricingService pricingService) {
        this.rentalRepo = rentalRepo;
        this.carRepo = carRepo;
        this.customerRepo = customerRepo;
        this.paymentRepo = paymentRepo;
        this.pricingService = pricingService;
    }
    public void createRental(Rental rental)
            throws SQLException, CarNotAvailableException, RentalOverlapException, InvalidDriverAgeException {

        Customer customer = customerRepo.findById(rental.getCustomerId());
        if (customer == null) {
            throw new RuntimeException("Customer not found with ID: " + rental.getCustomerId());
        }
        if (customer.getAge() < 18) {
            throw new InvalidDriverAgeException(customer.getAge());
        }
        Car car = carRepo.findById(rental.getCarId());
        if (car == null || !car.isAvailable()) {
            throw new CarNotAvailableException(rental.getCarId());
        }
        if (rentalRepo.hasOverlappingRental(rental.getCarId(), rental.getStartDate(), rental.getEndDate())) {
            throw new RentalOverlapException(rental.getCarId(), rental.getStartDate(), rental.getEndDate());
        }
        rentalRepo.add(rental);
    }
    public Payment completeRental(int rentalId, boolean hadAccident, String paymentMethod) throws SQLException {
        Rental rental = rentalRepo.findById(rentalId);
        if (rental == null) {
            throw new RuntimeException("Rental record not found.");
        }
        Car car = carRepo.findById(rental.getCarId());
        if (car == null) {
            throw new RuntimeException("Car record not found.");
        }
        BigDecimal finalAmount = pricingService.calculateTotal(car, rental, hadAccident);
        Payment payment = new Payment(rentalId, finalAmount, paymentMethod);
        paymentRepo.add(payment);
        rental.setTotalCost(finalAmount);
        rental.setStatus("completed");
        car.setAvailable(true);

        return payment;
    }
    public void cancelRental(int id) throws SQLException {
        Rental rental = rentalRepo.findById(id);
        if (rental == null) throw new RuntimeException("Rental ID " + id + " not found.");
        rental.setStatus("cancelled");
    }

    public List<Rental> getAllRentals() throws SQLException {
        return rentalRepo.findAll();
    }
}