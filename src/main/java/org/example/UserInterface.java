package org.example;

import java.util.List;
import java.util.Scanner;

public class UserInterface {
    private Dealership dealership;
    private static final Scanner scanner = new Scanner(System.in);

    public UserInterface() {
    }

    private void init() {
        System.out.println("Looking for file in: " + new java.io.File("src/main/resources/inventory.csv").getAbsolutePath());
        DealershipFileManager fileManager = new DealershipFileManager();
        this.dealership = fileManager.getDealership();

        if (dealership == null) {
            System.out.println("FATAL: Could not load dealership data. Exiting.");
            System.exit(1);
        }
    }

    public void display() {
        init();

        System.out.println("========================================");
        System.out.println("  Welcome to " + dealership.getName());
        System.out.println("  " + dealership.getAddress());
        System.out.println("  " + dealership.getPhone());
        System.out.println("========================================");

        int choice = 0;
        do {
            printMenu();
            choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1  -> processGetByPriceRequest();
                case 2  -> processGetByMakeModelRequest();
                case 3  -> processGetByYearRequest();
                case 4  -> processGetByColorRequest();
                case 5  -> processGetByMileageRequest();
                case 6  -> processGetByVehicleTypeRequest();
                case 7  -> processGetAllVehiclesRequest();
                case 8  -> processAddVehicleRequest();
                case 9  -> processRemoveVehicleRequest();
                case 99 -> System.out.println("\nThank you for using the Dealership App. Goodbye!");
                default -> System.out.println("Invalid option. Please try again.");
            }

        } while (choice != 99);
    }

    private void printMenu() {
        System.out.println("\n========================================");
        System.out.println("              MAIN MENU");
        System.out.println("========================================");
        System.out.println("  1  - Find vehicles within a price range");
        System.out.println("  2  - Find vehicles by make / model");
        System.out.println("  3  - Find vehicles by year range");
        System.out.println("  4  - Find vehicles by color");
        System.out.println("  5  - Find vehicles by mileage range");
        System.out.println("  6  - Find vehicles by type (car, truck, SUV, van)");
        System.out.println("  7  - List ALL vehicles");
        System.out.println("  8  - Add a vehicle");
        System.out.println("  9  - Remove a vehicle");
        System.out.println(" 99  - Quit");
        System.out.println("========================================");
    }

    private void displayVehicles(List<Vehicle> vehicles) {
        if (vehicles == null || vehicles.isEmpty()) {
            System.out.println("\n  No vehicles found matching your criteria.");
            return;
        }

        System.out.println();
        System.out.printf("%-8s | %-4s | %-10s | %-12s | %-6s | %-8s | %10s | %s%n",
                "VIN", "Year", "Make", "Model", "Type", "Color", "Odometer", "Price");
        System.out.println("-".repeat(80));

        for (Vehicle v : vehicles) {
            System.out.println(v);
        }

        System.out.println("-".repeat(80));
        System.out.println("  Total: " + vehicles.size() + " vehicle(s) found.");
    }

    public void processGetByPriceRequest() {
        System.out.println("\n--- Search by Price Range ---");
        double min = readDouble("Enter minimum price: $");
        double max = readDouble("Enter maximum price: $");
        List<Vehicle> results = dealership.getVehiclesByPrice(min, max);
        displayVehicles(results);
    }

    public void processGetByMakeModelRequest() {
        System.out.println("\n--- Search by Make / Model ---");
        String make  = readString("Enter make (e.g. Ford): ");
        String model = readString("Enter model (e.g. Explorer): ");
        List<Vehicle> results = dealership.getVehiclesByMakeModel(make, model);
        displayVehicles(results);
    }

    public void processGetByYearRequest() {
        System.out.println("\n--- Search by Year Range ---");
        int min = readInt("Enter minimum year: ");
        int max = readInt("Enter maximum year: ");
        List<Vehicle> results = dealership.getVehiclesByYear(min, max);
        displayVehicles(results);
    }

    public void processGetByColorRequest() {
        System.out.println("\n--- Search by Color ---");
        String color = readString("Enter color (e.g. Red): ");
        List<Vehicle> results = dealership.getVehiclesByColor(color);
        displayVehicles(results);
    }

    public void processGetByMileageRequest() {
        System.out.println("\n--- Search by Mileage Range ---");
        int min = readInt("Enter minimum mileage: ");
        int max = readInt("Enter maximum mileage: ");
        List<Vehicle> results = dealership.getVehiclesByMileage(min, max);
        displayVehicles(results);
    }

    public void processGetByVehicleTypeRequest() {
        System.out.println("\n--- Search by Vehicle Type ---");
        System.out.println("  Types: car, truck, SUV, van");
        String type = readString("Enter vehicle type: ");
        List<Vehicle> results = dealership.getVehiclesByType(type);
        displayVehicles(results);
    }

    public void processGetAllVehiclesRequest() {
        System.out.println("\n--- All Vehicles in Inventory ---");
        List<Vehicle> results = dealership.getAllVehicles();
        displayVehicles(results);
    }

    public void processAddVehicleRequest() {
        System.out.println("\n--- Add a Vehicle ---");

        int    vin         = readInt("Enter VIN: ");
        int    year        = readInt("Enter year: ");
        String make        = readString("Enter make: ");
        String model       = readString("Enter model: ");
        String vehicleType = readString("Enter type (car, truck, SUV, van): ");
        String color       = readString("Enter color: ");
        int    odometer    = readInt("Enter odometer reading: ");
        double price       = readDouble("Enter price: $");

        Vehicle newVehicle = new Vehicle(vin, year, make, model, vehicleType, color, odometer, price);
        dealership.addVehicle(newVehicle);

        DealershipFileManager fileManager = new DealershipFileManager();
        fileManager.saveDealership(dealership);

        System.out.println("\n  Vehicle added successfully!");
        displayVehicles(List.of(newVehicle));
    }

    public void processRemoveVehicleRequest() {
        System.out.println("\n--- Remove a Vehicle ---");
        int vin = readInt("Enter the VIN of the vehicle to remove: ");
        Vehicle toRemove = null;
        for (Vehicle v : dealership.getAllVehicles()) {
            if (v.getVin() == vin) {
                toRemove = v;
                break;
            }
        }

        if (toRemove == null) {
            System.out.println("  No vehicle found with VIN: " + vin);
            return;
        }

        dealership.removeVehicle(toRemove);

        DealershipFileManager fileManager = new DealershipFileManager();
        fileManager.saveDealership(dealership);

        System.out.println("  Vehicle with VIN " + vin + " has been removed from inventory.");
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("  Please enter a valid whole number.");
            }
        }
    }

    private double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("  Please enter a valid number.");
            }
        }
    }

    private String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
}
