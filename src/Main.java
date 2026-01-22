import oop.assignment.db.IDB;
import oop.assignment.db.PostgresDB;
import oop.assignment.entities.*;
import oop.assignment.exceptions.*;
import oop.assignment.repositories.*;
import oop.assignment.repositories.interfaces.*;
import oop.assignment.services.*;

import java.math.BigDecimal;
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
                System.out.println("\n--- CAR RENTAL MANAGEMENT SYSTEM ---");
                System.out.println("1. Search available cars");
                System.out.println("2. Create a rental");
                System.out.println("3. Complete rental with payment");
                System.out.println("4. Add new car");
                System.out.println("5. Register new customer");
                System.out.println("6. View all cars");
                System.out.println("7. View all customers");
                System.out.println("8. View all rentals");
                System.out.println("9. View all payments");
                System.out.println("0. Exit");
                System.out.print("Select an option: ");

                int choice = scanner.nextInt();
                scanner.nextLine();
                if (choice == 0) break;
                try {
                    switch (choice) {
                        case 1:
                            System.out.print("\n--- AVAILABLE CARS ---\n");
                            List<Car> availableCars = carInventoryService.getOnlyAvailableCars();
                            if (availableCars.isEmpty()) {
                                System.out.println("No cars available at the moment.");
                            } else {
                                availableCars.forEach(car -> System.out.println(
                                        "ID: " + car.getId() + " | " + car.getMake() + " " + car.getModel() +
                                                " | Rate: " + car.getRate() + " KZT/day"
                                ));
                            }
                            break;
                        case 2:
                            System.out.print("Car ID: ");
                            int carId = scanner.nextInt();
                            System.out.print("Customer ID: ");
                            int customerId = scanner.nextInt();
                            scanner.nextLine();
                            System.out.print("Start date (YYYY-MM-DD): ");
                            LocalDate startDate = LocalDate.parse(scanner.nextLine());
                            System.out.print("End date (YYYY-MM-DD): ");
                            LocalDate endDate = LocalDate.parse(scanner.nextLine());

                            Rental rental = new Rental(carId, customerId, startDate, endDate);
                            rentalService.createRental(rental);
                            System.out.println("Success! Rental created. Rental ID: " + rental.getId());
                            break;
                        case 3:
                            System.out.print("Rental ID: ");
                            int rentalId = scanner.nextInt();
                            scanner.nextLine();
                            System.out.print("Was there an accident? (yes/no): ");
                            boolean hadAccident = scanner.nextLine().equalsIgnoreCase("yes");
                            System.out.print("Payment method (cash/card): ");
                            String paymentMethod = scanner.nextLine();

                            Payment payment = rentalService.completeRental(rentalId, hadAccident, paymentMethod);
                            System.out.println("Success! Rental completed.");
                            System.out.println("Total cost: " + payment.getAmount() + " KZT");
                            System.out.println("Payment ID: " + payment.getId());
                            break;
                        case 4:
                            System.out.print("Car make: ");
                            String make = scanner.nextLine();
                            System.out.print("Car model: ");
                            String model = scanner.nextLine();
                            System.out.print("Daily rate (KZT): ");
                            BigDecimal rate = new BigDecimal(scanner.nextLine());
                            System.out.print("Available? (yes/no): ");
                            boolean available = scanner.nextLine().equalsIgnoreCase("yes");

                            Car newCar = new Car(make, model, rate, available);
                            carRepo.add(newCar);
                            System.out.println("Success! Car added. Car ID: " + newCar.getId());
                            break;
                        case 5:
                            System.out.print("Full name: ");
                            String fullName = scanner.nextLine();
                            System.out.print("Email: ");
                            String email = scanner.nextLine();
                            System.out.print("Driver license ID: ");
                            String license = scanner.nextLine();
                            System.out.print("Birthdate (YYYY-MM-DD): ");
                            LocalDate birthdate = LocalDate.parse(scanner.nextLine());

                            Customer newCustomer = new Customer(fullName, email, license, birthdate);
                            customerRepo.add(newCustomer);
                            System.out.println("Success! Customer registered. Customer ID: " + newCustomer.getId());
                            break;
                        case 6:
                            System.out.print("\n--- ALL CARS ---\n");
                            List<Car> allCars = carRepo.findAll();
                            if (allCars.isEmpty()) {
                                System.out.println("No cars in system.");
                            } else {
                                allCars.forEach(car -> System.out.println(
                                        "ID: " + car.getId() + " | " + car.getMake() + " " + car.getModel() +
                                                " | Rate: " + car.getRate() + " KZT/day | Available: " + (car.isAvailable() ? "Yes" : "No")
                                ));
                            }
                            break;
                        case 7:
                            System.out.print("\n--- ALL CUSTOMERS ---\n");
                            List<Customer> allCustomers = customerRepo.findAll();
                            if (allCustomers.isEmpty()) {
                                System.out.println("No customers in system.");
                            } else {
                                allCustomers.forEach(customer -> System.out.println(
                                        "ID: " + customer.getId() + " | Name: " + customer.getFullName() +
                                                " | Age: " + customer.getAge() + " | Email: " + customer.getEmail()
                                ));
                            }
                            break;
                        case 8:
                            System.out.print("\n--- ALL RENTALS ---\n");
                            List<Rental> allRentals = rentalRepo.findAll();
                            if (allRentals.isEmpty()) {
                                System.out.println("No rentals in system.");
                            } else {
                                allRentals.forEach(rentalItem -> System.out.println(
                                        "ID: " + rentalItem.getId() + " | Car: " + rentalItem.getCarId() +
                                                " | Customer: " + rentalItem.getCustomerId() + " | " +
                                                rentalItem.getStartDate() + " to " + rentalItem.getEndDate() +
                                                " | Status: " + rentalItem.getStatus()
                                ));
                            }
                            break;
                        case 9:
                            System.out.print("\n--- ALL PAYMENTS ---\n");
                            List<Payment> allPayments = paymentRepo.getAllPayments();
                            if (allPayments.isEmpty()) {
                                System.out.println("No payments in system.");
                            } else {
                                allPayments.forEach(paymentItem -> System.out.println(
                                        "ID: " + paymentItem.getId() + " | Rental: " + paymentItem.getRentalId() +
                                                " | Amount: " + paymentItem.getAmount() + " KZT" +
                                                " | Method: " + paymentItem.getPaymentMethod()
                                ));
                            }
                            break;
                    }
                } catch (InvalidDriverAgeException e) {
                    System.out.println("Error: " + e.getMessage());
                } catch (CarNotAvailableException e) {
                    System.out.println("Error: " + e.getMessage());
                } catch (RentalOverlapException e) {
                    System.out.println("Error: " + e.getMessage());
                } catch (InvalidPaymentAmount e) {
                    System.out.println("Error: " + e.getMessage());
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }
    }
}