package com.cognizant.lenderservice;

import java.time.LocalDate;
import java.util.Objects;

public class CandidateLoan {
    public int creditScore;
    private float debtToIncome;
    private double savings;
    private double requestedAmount;
    private String qualification;
    private double loan_amount;
    private String status;
    private boolean isAccept;
    private boolean decided = false;
    private LocalDate expireDate;

    public CandidateLoan(int creditScore, float debtToIncome, double savings, double requestedAmount) {
        this.creditScore = creditScore;
        this.debtToIncome = debtToIncome;
        this.savings = savings;
        this.requestedAmount = requestedAmount;
    }

    public int getCreditScore() {
        return creditScore;
    }

    public void setCreditScore(int creditScore) {
        this.creditScore = creditScore;
    }

    public float getDebtToIncome() {
        return debtToIncome;
    }

    public void setDebtToIncome(float debtToIncome) {
        this.debtToIncome = debtToIncome;
    }

    public double getSavings() {
        return savings;
    }

    public void setSavings(double savings) {
        this.savings = savings;
    }

    public double getRequestedAmount() {
        return requestedAmount;
    }

    public void setRequestedAmount(double requestedAmount) {
        this.requestedAmount = requestedAmount;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public double getLoan_amount() {
        return loan_amount;
    }

    public void setLoan_amount(double loan_amount) {
        this.loan_amount = loan_amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean acceptOrRejectOffer(boolean isAccept) {
        this.isAccept=isAccept;
        this.decided=true;
        return isAccept;
    }

    public boolean isAccept() {
        return isAccept;
    }

    public boolean isDecided() {
        return decided;
    }

    public LocalDate getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(LocalDate expireDate) {
        this.expireDate = expireDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CandidateLoan that = (CandidateLoan) o;
        return creditScore == that.creditScore && Float.compare(that.debtToIncome, debtToIncome) == 0 && Double.compare(that.savings, savings) == 0 && Double.compare(that.requestedAmount, requestedAmount) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(creditScore, debtToIncome, savings, requestedAmount);
    }

    @Override
    public String toString() {
        return "Candidate{" +
                "creditScore=" + creditScore +
                ", debtToIncome=" + debtToIncome +
                ", savings=" + savings +
                ", requestedAmount=" + requestedAmount +
                ", qualification='" + qualification + '\'' +
                ", loan_amount=" + loan_amount +
                ", status='" + status + '\'' +
                ", isAccept=" + isAccept +
                ", decided=" + decided +
                ", expireDate=" + expireDate +
                '}';
    }
}
