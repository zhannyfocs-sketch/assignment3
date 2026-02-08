package oop.assignment.RentalComponent;

import oop.assignment.BillingComponent.IPaymentRepository;
import oop.assignment.FleetComponent.Car;
import oop.assignment.FleetComponent.ICarRepository;
import oop.assignment.RentalComponent.exceptions.*;
import oop.assignment.BillingComponent.PricingService;

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
        if (rental.getStartDate().isBefore(java.time.LocalDate.now())) {
            throw new IllegalArgumentException("Start date cannot be in the past.");
        }
        if (rental.getRentalDays() <= 0) {
            throw new IllegalArgumentException("End date must be after start date. Total days must be at least 1.");
        }
        if (customer.getAge() < RentalPolicy.getInstance().getMinDriverAge()) {
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
        carRepo.updateAvailability(rental.getCarId(),false);
        System.out.println("Succes! Car #" + rental.getCarId() + " is now rented.");
    }

    public void completeRental(int rentalId, boolean hadAccident) throws SQLException {
        Rental rental = rentalRepo.findById(rentalId);
        Car car = carRepo.findById(rental.getCarId());
        java.math.BigDecimal finalPrice = pricingService.calculateTotal(car, rental, hadAccident);
        oop.assignment.BillingComponent.Payment payment = new oop.assignment.BillingComponent.Payment(
                rentalId,
                finalPrice,
                "Credit Card"
        );
        paymentRepo.add(payment);
        rentalRepo.updateStatus(rentalId, "completed");
        carRepo.updateAvailability(car.getId(), true);

        System.out.println("Rental complete. Total: " + finalPrice);
    }
}