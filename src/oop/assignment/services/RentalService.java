package oop.assignment.services;

import oop.assignment.entities.*;
import oop.assignment.exceptions.*;
import oop.assignment.repositories.interfaces.*;
import oop.assignment.config.FleetConfig;
import java.sql.SQLException;
import java.util.List;

public class RentalService {
    private final IRentalRepository rentalRepo;
    private final ICarRepository carRepo;
    private final IPaymentRepository paymentRepo;
    private final ICustomerRepository customerRepo;
    private final PricingService pricingService;

    public RentalService(IRentalRepository rentalRepo, ICarRepository carRepo,IPaymentRepository paymentRepo,
                         ICustomerRepository customerRepo, PricingService pricingService) {
        this.rentalRepo = rentalRepo;
        this.carRepo = carRepo;
        this.paymentRepo = paymentRepo;
        this.customerRepo = customerRepo;
        this.pricingService = pricingService;
    }
    public List<Rental> getAllRentals() throws SQLException {
        return rentalRepo.findAll();
    }
    public void createRental(Rental rental) throws SQLException, CarNotAvailableException,
            RentalOverlapException, InvalidDriverAgeException {
        Customer customer = customerRepo.findById(rental.getCustomerId());
        if (customer.getAge() < FleetConfig.getInstance().getMinDriverAge()) {
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

    public void completeRental(int rentalId, boolean hadAccident) throws SQLException {
        Rental rental = rentalRepo.findById(rentalId);
        Car car = carRepo.findById(rental.getCarId());
        java.math.BigDecimal finalPrice = pricingService.calculateTotal(car, rental, hadAccident);
        rentalRepo.updateStatus(rentalId, "completed");
        carRepo.updateAvailability(car.getId(), true);

        System.out.println("Rental complete. Total: " + finalPrice);
    }
}