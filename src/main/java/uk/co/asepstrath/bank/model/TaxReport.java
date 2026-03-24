package uk.co.asepstrath.bank.model;

import java.util.List;

public class TaxReport {
    private final int year;
    private final List<TaxSaleLine> sales;
    private final double totalGains;
    private final double totalLosses;
    private final double netGains;
    private final double allowance;
    private final double taxableGains;
    private final double taxRate;
    private final double taxOwed;
    private final double allowanceUsed;
    private final double allowanceRemaining;

    public TaxReport(int year, List<TaxSaleLine> sales) {
        this.year = year;
        this.sales = sales;
        this.allowance = 3000.0;
        this.taxRate = 0.24;

        this.totalGains = round(sales.stream()
                .mapToDouble(TaxSaleLine::getGainLoss)
                .filter(g -> g > 0)
                .sum());

        this.totalLosses = round(sales.stream()
                .mapToDouble(TaxSaleLine::getGainLoss)
                .filter(g -> g < 0)
                .sum());

        this.netGains = round(totalGains + totalLosses);
        this.allowanceUsed = round(Math.min(Math.max(netGains, 0), allowance));
        this.allowanceRemaining = round(allowance - allowanceUsed);
        this.taxableGains = round(Math.max(0, netGains - allowance));
        this.taxOwed = round(taxableGains * taxRate);
    }

    private double round(double v) {
        return Math.round(v * 100.0) / 100.0;
    }

    public int getYear() {
        return year;
    }

    public List<TaxSaleLine> getSales() {
        return sales;
    }

    public double getTotalGains() {
        return totalGains;
    }

    public double getTotalLosses() {
        return totalLosses;
    }

    public double getNetGains() {
        return netGains;
    }

    public double getAllowance() {
        return allowance;
    }

    public double getTaxableGains() {
        return taxableGains;
    }

    public double getTaxRate() {
        return taxRate;
    }

    public double getTaxOwed() {
        return taxOwed;
    }

    public double getAllowanceUsed() {
        return allowanceUsed;
    }

    public double getAllowanceRemaining() {
        return allowanceRemaining;
    }
}