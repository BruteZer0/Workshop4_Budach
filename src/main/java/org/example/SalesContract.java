package org.example;

public class SalesContract extends Contract {
    private boolean finance;

    private static final double SALES_TAX_RATE = 0.05;
    private static final double RECORDING_FEE = 100.00;

    public SalesContract(String date, String customerName, String customerEmail,
                         Vehicle vehicle, boolean finance) {
        super(date, customerName, customerEmail, vehicle);
        this.finance = finance;
    }

    public boolean isFinance() {
        return finance;
    }

    public void setFinance(boolean finance) {
        this.finance = finance;
    }

    @Override
    public double getTotalPrice() {
        double price = getVehicle().getPrice();
        double tax = price * SALES_TAX_RATE;
        double processingFee = price < 10000 ? 295.00 : 495.00;
        return price + tax + RECORDING_FEE + processingFee;
    }

    @Override
    public double getMonthlyPayment() {
        if (!finance) return 0.00;

        double totalPrice = getTotalPrice();
        double rate;
        int months;

        if (totalPrice >= 10000) {
            rate = 0.0425 / 12;
            months = 48;
        } else {
            rate = 0.0525 / 12;
            months = 24;
        }

        return (totalPrice * rate) / (1 - Math.pow(1 + rate, -months));
    }
}
