import oop.assignment.db.IDB;
import oop.assignment.db.PostgresDB;
import oop.assignment.entities.*;
import oop.assignment.exceptions.*;
import oop.assignment.repositories.*;
import oop.assignment.repositories.interfaces.*;
import oop.assignment.services.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        IDB db = new PostgresDB();
        System.out.println("Connecting to Supabase...");
        try (Connection connection = db.getConnection()) {
            System.out.println("Connected successfully!");
            String sqlCheck = "SELECT CURRENT_TIMESTAMP";
            try (PreparedStatement stmt = connection.prepareStatement(sqlCheck);
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Database time: " + rs.getTimestamp(1));
                }
            }

            ICarRepository carRepo = new CarRepository(db);
            ICustomerRepository customerRepo = new CustomerRepository(db);
            IRentalRepository rentalRepo = new RentalRepository(db);
            IPaymentRepository paymentRepo = new PaymentRepository(db);

            PricingService pricingService = new PricingService();
            CarInventoryService carInventoryService = new CarInventoryService(carRepo);
            RentalService rentalService = new RentalService(rentalRepo, carRepo, customerRepo, paymentRepo, pricingService);

            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("\n--- Car Rental System ---");
                System.out.println("1. Register Customer");
                System.out.println("2. Create Rental");
                System.out.println("3. Complete Rental with Payment");
                System.out.println("4. Search Available Cars");
                System.out.println("5. Cancel Rental");
                System.out.println("6. View All Customers");
                System.out.println("7. View All Rentals");
                System.out.println("0. Exit");
                System.out.print("Select an option: ");

                int choice = scanner.nextInt();
                scanner.nextLine();
                if (choice == 0) break;
                try {
                    switch (choice) {
                        case 1:
                            System.out.print("Full Name: "); String name = scanner.nextLine();
                            System.out.print("Email: "); String email = scanner.nextLine();
                            System.out.print("Driver License: "); String license = scanner.nextLine();
                            System.out.print("Birthdate (YYYY-MM-DD): "); LocalDate birthdate = LocalDate.parse(scanner.nextLine());
                            int age = java.time.Period.between(birthdate, java.time.LocalDate.now()).getYears();
                            if (age < 18) {
                                System.out.println("Error: Customer must be 18 or older. Age: " + age);
                            } else {
                                Customer c = new Customer(name, email, license, birthdate);
                                customerRepo.add(c);
                                System.out.println("Success! Customer registered. Assigned ID: " + c.getId());
                            }
                            break;
                        case 2:
                            System.out.print("Car ID: "); int carId = scanner.nextInt();
                            System.out.print("Customer ID: "); int customerId = scanner.nextInt();
                            scanner.nextLine();
                            System.out.print("Start Date (YYYY-MM-DD): "); LocalDate start = LocalDate.parse(scanner.nextLine());
                            System.out.print("End Date (YYYY-MM-DD): "); LocalDate end = LocalDate.parse(scanner.nextLine());
                            Rental r = new Rental(carId, customerId, start, end);
                            if (!r.isValidDates()) {
                                System.out.println("Error: End date must be after start date.");
                            } else {
                                rentalService.createRental(r);
                                System.out.println("Success! Rental created. Rental ID: " + r.getId());
                            }
                            break;
                        case 3:
                            System.out.print("Rental ID: "); int rentalId = scanner.nextInt();
                            scanner.nextLine();
                            System.out.print("Accident? (yes/no): "); boolean accident = scanner.nextLine().equalsIgnoreCase("yes");
                            System.out.print("Payment Method: "); String method = scanner.nextLine();
                            Payment p = rentalService.completeRental(rentalId, accident, method);
                            System.out.println("Success! Rental completed. Payment: " + p.getAmount() + " KZT");
                            break;
                        case 4:
                            List<Car> cars = carInventoryService.getOnlyAvailableCars();
                            if (cars.isEmpty()) {
                                System.out.println("No cars available at the moment.");
                            } else {
                                System.out.println("--- Available Cars ---");
                                cars.forEach(car -> System.out.println("ID " + car.getId() + ": " + car.getMake() + " " + car.getModel() + " - " + car.getRate() + " KZT/day"));
                            }
                            break;
                        case 5:
                            System.out.print("Rental ID to cancel: ");
                            rentalService.cancelRental(scanner.nextInt());
                            System.out.println("Cancelled.");
                            break;
                        case 6:
                            List<Customer> customers = customerRepo.findAll();
                            if (customers.isEmpty()) {
                                System.out.println("No customers registered yet.");
                            } else {
                                System.out.println("--- Registered Customers ---");
                                customers.forEach(customer -> System.out.println("ID " + customer.getId() + ": " + customer.getFullName() + " - Age: " + customer.getAge()));
                            }
                            break;
                        case 7:
                            List<Rental> rentals = rentalService.getAllRentals();
                            if (rentals.isEmpty()) {
                                System.out.println("No rentals yet.");
                            } else {
                                System.out.println("--- All Rentals ---");
                                rentals.forEach(rental -> System.out.println("ID " + rental.getId() + ": Car " + rental.getCarId() + " - Customer " + rental.getCustomerId() + " - " + rental.getStatus()));
                            }
                            break;
                    }
                }
                catch (Exception e) {
                    System.out.println("--- DEBUG ERROR INFO ---");
                    e.printStackTrace();
                    System.out.println("------------------------");
                }
            }
        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }
    }
}