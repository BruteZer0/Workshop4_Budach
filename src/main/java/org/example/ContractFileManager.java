package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ContractFileManager {
    private static final String FILE_PATH = "src/main/resources/contracts.csv";

    public void saveContract(Contract contract) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH, true))) {

            Vehicle v = contract.getVehicle();

            String common = String.format("%s|%s|%s|%s|%d|%d|%s|%s|%s|%s|%d|%.2f",
                    contract instanceof SalesContract ? "SALE" : "LEASE",
                    contract.getDate(),
                    contract.getCustomerName(),
                    contract.getCustomerEmail(),
                    v.getVin(), v.getYear(), v.getMake(), v.getModel(),
                    v.getVehicleType(), v.getColor(), v.getOdometer(), v.getPrice());

            if (contract instanceof SalesContract sc) {
                writer.printf("%s|%.2f|%.2f|%.2f|%.2f|%s|%.2f%n",
                        common,
                        v.getPrice() * 0.05,
                        100.00,
                        v.getPrice() < 10000 ? 295.00 : 495.00,
                        sc.getTotalPrice(),
                        sc.isFinance() ? "YES" : "NO",
                        sc.getMonthlyPayment());

            } else if (contract instanceof LeaseContract lc) {
                writer.printf("%s|%.2f|%.2f|%.2f|%.2f%n",
                        common,
                        lc.getExpectedEndingValue(),
                        lc.getLeaseFee(),
                        lc.getTotalPrice(),
                        lc.getMonthlyPayment());
            }

        } catch (IOException e) {
            System.out.println("ERROR: Could not save contract: " + e.getMessage());
        }
    }
}
