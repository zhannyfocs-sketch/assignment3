package oop.assignment.UIComponent;

import oop.assignment.BillingComponent.*;
import oop.assignment.FleetComponent.Car;
import oop.assignment.FleetComponent.CarInventoryService;
import oop.assignment.FleetComponent.CarRepository;
import oop.assignment.FleetComponent.ICarRepository;
import oop.assignment.RentalComponent.*;
import oop.assignment.RentalComponent.exceptions.*;
import oop.assignment.ReportingComponent.ReportService;
import oop.assignment.db.IDB;
import oop.assignment.db.PostgresDB;
import oop.assignment.RentalComponent.Customer;
import oop.assignment.RentalComponent.Rental;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        IDB db = PostgresDB.getInstance();

        ICarRepository carRepo = new CarRepository(db);
        ICustomerRepository customerRepo = new CustomerRepository(db);
        IRentalRepository rentalRepo = new RentalRepository(db);
        IPaymentRepository paymentRepo = new PaymentRepository(db);

        PricingService pricingService = new PricingService();
        CarInventoryService carInventoryService = new CarInventoryService(carRepo);
        RentalService rentalService = new RentalService(rentalRepo, carRepo, paymentRepo, customerRepo, pricingService);
        ReportService reportService = new ReportService(paymentRepo, rentalRepo);

        Scanner scanner = new Scanner(System.in);

        System.out.println("--- System Initialized ---");

        while (true) {
            System.out.println("\n--- CAR RENTAL MANAGEMENT SYSTEM ---");
            System.out.println("1. Register Customer");
            System.out.println("2. Create Rental");
            System.out.println("3. Complete Rental");
            System.out.println("4. Search Available Cars");
            System.out.println("5. View All Rentals");
            System.out.println("6. View Financial Summary ");
            System.out.println("0. Exit");
            System.out.print("Select an option: ");

            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine();
            if (choice == 0) break;

            try {
                switch (choice) {
                    case 1:
                        System.out.print("Full Name: "); String name = scanner.nextLine();
                        System.out.print("Email: "); String email = scanner.nextLine();
                        System.out.print("Driver License ID: "); String license = scanner.nextLine();
                        System.out.print("Birthdate (YYYY-MM-DD): ");
                        LocalDate birthdate = LocalDate.parse(scanner.nextLine());

                        int age = java.time.Period.between(birthdate, LocalDate.now()).getYears();
                        if (age < RentalPolicy.getInstance().getMinDriverAge()) {
                            throw new InvalidDriverAgeException(age);
                        }

                        Customer c = new Customer.Builder()
                                .setFullName(name)
                                .setEmail(email)
                                .setDriverLicenseId(license)
                                .setBirthdate(birthdate)
                                .build();
                        customerRepo.add(c);
                        System.out.println("Success: Customer " + c.getId() + " registered.");
                        break;

                    case 2:
                        System.out.print("Car ID: "); int carId = scanner.nextInt();
                        System.out.print("Customer ID: "); int customerId = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Start Date (YYYY-MM-DD): "); LocalDate start = LocalDate.parse(scanner.nextLine());
                        System.out.print("End Date (YYYY-MM-DD): "); LocalDate end = LocalDate.parse(scanner.nextLine());

                        Rental r = new Rental.Builder()
                                .setCarId(carId)
                                .setCustomerId(customerId)
                                .setStartDate(start)
                                .setEndDate(end)
                                .setStatus("active")
                                .build();

                        rentalService.createRental(r);
                        System.out.println("Success: Rental number " + r.getId() + " created. Car marked as RENTED.");
                        break;

                    case 3:
                        System.out.print("Rental ID to complete: ");
                        int rid = Integer.parseInt(scanner.nextLine());

                        System.out.print("Was there an accident? (yes/no): ");
                        boolean accident = scanner.nextLine().equalsIgnoreCase("yes");

                        Rental rent = rentalRepo.findById(rid);
                        Car rentedCar = carRepo.findById(rent.getCarId());
                        BigDecimal finalAmount = pricingService.calculateTotal(rentedCar, rent, accident);

                        System.out.println("\nTotal Amount Due: " + finalAmount);

                        System.out.println("Select Payment Method:");
                        System.out.println("1. Credit Card");
                        System.out.println("2. Cash / Bank Transfer");
                        System.out.print("Option: ");
                        int method = Integer.parseInt(scanner.nextLine());

                        if (method == 1) {
                            System.out.print("Enter Card Number: "); String card = scanner.nextLine();
                            System.out.print("Enter Expiry (MM/YY): "); String expiry = scanner.nextLine();
                            System.out.println("Processing Card [" + card + "]...");
                        } else {
                            System.out.println("Waiting for Cash/Transfer confirmation...");
                        }

                        rentalService.completeRental(rid,accident);
                        System.out.println("\n[✔] Payment Successful! Rental closed.");
                        break;

                    case 4:
                        List<Car> cars = carInventoryService.getOnlyAvailableCars();
                        System.out.println("\n--- Current Available Inventory ---");
                        if (cars.isEmpty()) System.out.println("No cars currently available.");
                        cars.forEach(car1 -> System.out.println("ID: " + car1.getId() + " | " + car1.getModel() + " [" + car1.getType() + "]"));
                        break;

                    case 5:
                        System.out.println("\n--- All Rental Records ---");
                        rentalService.getAllRentals().forEach(System.out::println);
                        break;

                    case 6:
                        reportService.generateFinancialSummary();
                        break;

                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            } catch (CarNotAvailableException | RentalOverlapException | InvalidDriverAgeException e) {
                System.out.println("\nBUSINESS RULE VIOLATION: " + e.getMessage());
            } catch (SQLException e) {
                System.out.println("\nDATABASE ERROR: Connection lost or SQL syntax error. " + e.getMessage());
            } catch (Exception e) {
                System.out.println("\nUNEXPECTED ERROR: " + e.getMessage());
                e.printStackTrace();
            }
        }
        System.out.println("Closing system. Goodbye!");
    }
}