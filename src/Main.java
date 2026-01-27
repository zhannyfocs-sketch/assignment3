import oop.assignment.singletones.FleetConfig;
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
        IDB db = PostgresDB.getInstance();

        System.out.println("Connecting to Database...");
        try (Connection connection = db.getConnection()) {
            System.out.println("Connected successfully!");

            ICarRepository carRepo = new CarRepository(db);
            ICustomerRepository customerRepo = new CustomerRepository(db);
            IRentalRepository rentalRepo = new RentalRepository(db);
            IPaymentRepository paymentRepo = new PaymentRepository(db);

            PricingService pricingService = new PricingService();
            CarInventoryService carInventoryService = new CarInventoryService(carRepo);
            RentalService rentalService = new RentalService(rentalRepo, carRepo,paymentRepo, customerRepo, pricingService);

            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("\n--- CAR RENTAL MANAGEMENT SYSTEM ---\n");
                System.out.println("1. Register Customer");
                System.out.println("2. Create Rental ");
                System.out.println("3. Complete Rental with Payment");
                System.out.println("4. Search Available Cars");
                System.out.println("5. View All Rentals");
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

                            int age = java.time.Period.between(birthdate, LocalDate.now()).getYears();
                            if (age < FleetConfig.getInstance().getMinDriverAge()) {
                                throw new InvalidDriverAgeException(age);
                            }
                            Customer c = new Customer.Builder()
                                    .setFullName(name)
                                    .setEmail(email)
                                    .setDriverLicenseId(license)
                                    .setBirthdate(birthdate)
                                    .build();
                            customerRepo.add(c);
                            System.out.println("Success! Customer registered.");
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
                            System.out.println("Success! Rental created.");
                            break;
                        case 3:
                            System.out.print("Rental ID: "); int rId = scanner.nextInt();
                            scanner.nextLine();
                            System.out.print("Accident? (yes/no): "); boolean accident = scanner.nextLine().equalsIgnoreCase("yes");
                            rentalService.completeRental(rId, accident);
                            System.out.println("Success! Rental completed.");
                            break;
                        case 4:
                            List<Car> cars = carInventoryService.getOnlyAvailableCars();
                            System.out.println("--- Available Cars ---");
                            cars.forEach(car -> System.out.println(car.getId() + ": " + car.getModel() + " (" + car.getType() + ")"));
                            break;
                        case 5:
                            rentalService.getAllRentals().forEach(rental -> System.out.println(rental));
                            break;
                    }
                }
                catch (CarNotAvailableException | RentalOverlapException | InvalidDriverAgeException e) {
                    System.out.println("BUSINESS RULE ERROR: " + e.getMessage());
                } catch (InvalidPaymentAmount e) {
                    System.out.println("PAYMENT ERROR: " + e.getMessage());
                } catch (Exception e) {
                    System.out.println("SYSTEM ERROR: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            System.out.println("Database Connection Error: " + e.getMessage());
        }
    }
}