package com.cognizant.lenderservice;

public class Candidate {
    public int creditScore;
    private float debtToIncome;
    private double savings;
    private double requestedAmount;
    private String qualification;
    private double loan_amount;
    private String status;
    private boolean isAccept;

    public Candidate(int creditScore, float debtToIncome, double savings, double requestedAmount) {
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
        return isAccept;
    }

    public boolean isAccept() {
        return isAccept;
    }
}
