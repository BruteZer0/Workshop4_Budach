package org.example;

public class LeaseContract extends Contract {

    public LeaseContract(String date, String customerName, String customerEmail,
                         Vehicle vehicle) {
        super(date, customerName, customerEmail, vehicle);
    }

    public double getExpectedEndingValue() {
        return getVehicle().getPrice() * 0.50;
    }

    public double getLeaseFee() {
        return getVehicle().getPrice() * 0.07;
    }

    @Override
    public double getTotalPrice() {
        return getExpectedEndingValue() + getLeaseFee();
    }

    @Override
    public double getMonthlyPayment() {
        double rate = 0.04 / 12;
        int months = 36;
        double totalPrice = getTotalPrice();
        return (totalPrice * rate) / (1 - Math.pow(1 + rate, -months));
    }
}
