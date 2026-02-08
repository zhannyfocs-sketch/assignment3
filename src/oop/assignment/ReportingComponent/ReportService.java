package oop.assignment.ReportingComponent;

import oop.assignment.BillingComponent.IPaymentRepository;

import oop.assignment.BillingComponent.Payment;
import oop.assignment.RentalComponent.IRentalRepository;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class ReportService {
    private final IPaymentRepository paymentRepo;
    private final IRentalRepository rentalRepo;

    public ReportService(IPaymentRepository paymentRepo, IRentalRepository rentalRepo) {
        this.paymentRepo = paymentRepo;
        this.rentalRepo = rentalRepo;
    }

    public void generateFinancialSummary() throws SQLException {
        List<Payment> payments = paymentRepo.findAll();

        BigDecimal totalRevenue = payments.stream()
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        System.out.println("\n========== BUSINESS REPORT ==========");
        System.out.println("Total Rentals Completed: " + payments.size());
        System.out.println("Total Revenue Earned:    $" + totalRevenue);
        System.out.println("=====================================");
    }
}