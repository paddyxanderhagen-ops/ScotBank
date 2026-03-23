package uk.co.asepstrath.bank.model;

import java.util.List;

public class TaxReport {
    private int year;
    private List<TaxSaleLine> sales;
    private double totalGains;
    private double totalLosses;
    private double netGains;
    private double allowance;
    private double taxableGains;
    private double taxRate;
    private double taxOwed;
    private double allowanceUsed;
    private double allowanceRemaining;

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

    public int getYear() { return year; }
    public List<TaxSaleLine> getSales() { return sales; }
    public double getTotalGains() { return totalGains; }
    public double getTotalLosses() { return totalLosses; }
    public double getNetGains() { return netGains; }
    public double getAllowance() { return allowance; }
    public double getTaxableGains() { return taxableGains; }
    public double getTaxRate() { return taxRate; }
    public double getTaxOwed() { return taxOwed; }
    public double getAllowanceUsed() { return allowanceUsed; }
    public double getAllowanceRemaining() { return allowanceRemaining; }
}