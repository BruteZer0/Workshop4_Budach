package org.example;

import java.io.*;
import java.util.List;

public class DealershipFileManager {
    private static final String FILE_PATH = "src/main/resources/inventory.csv";

    public Dealership getDealership() {
        Dealership dealership = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {

            String firstLine = reader.readLine();
            if (firstLine == null) {
                System.out.println("ERROR: Inventory file is empty.");
                return null;
            }

            String[] dealerParts = firstLine.split("\\|");
            if (dealerParts.length < 3) {
                System.out.println("ERROR: Invalid dealership header in inventory file.");
                return null;
            }

            dealership = new Dealership(dealerParts[0], dealerParts[1], dealerParts[2]);

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split("\\|");
                if (parts.length < 8) {
                    System.out.println("WARNING: Skipping malformed vehicle line: " + line);
                    continue;
                }

                try {
                    int vin = Integer.parseInt(parts[0].trim());
                    int year = Integer.parseInt(parts[1].trim());
                    String make = parts[2].trim();
                    String model = parts[3].trim();
                    String type = parts[4].trim();
                    String color = parts[5].trim();
                    int odometer = Integer.parseInt(parts[6].trim());
                    double price = Double.parseDouble(parts[7].trim());

                    Vehicle vehicle = new Vehicle(vin, year, make, model, type, color, odometer, price);
                    dealership.addVehicle(vehicle);
                } catch (NumberFormatException e) {
                    System.out.println("WARNING: Skipping vehicle with invalid data: " + line);
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("ERROR: Inventory file not found at path: " + FILE_PATH);
        } catch (IOException e) {
            System.out.println("ERROR: Could not read inventory file: " + e.getMessage());
        }

        return dealership;
    }

    public void saveDealership(Dealership dealership) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH, false))) {

            writer.println(dealership.getName() + "|" +
                    dealership.getAddress() + "|" +
                    dealership.getPhone());

            List<Vehicle> vehicles = dealership.getAllVehicles();
            for (Vehicle v : vehicles) {
                writer.printf("%d|%d|%s|%s|%s|%s|%d|%.2f%n",
                        v.getVin(),
                        v.getYear(),
                        v.getMake(),
                        v.getModel(),
                        v.getVehicleType(),
                        v.getColor(),
                        v.getOdometer(),
                        v.getPrice());
            }
        } catch (IOException e) {
            System.out.println("ERROR: Could not save inventory file: " + e.getMessage());
        }
    }
}
