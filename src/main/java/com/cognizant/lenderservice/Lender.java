package com.cognizant.lenderservice;

public class Lender {
    private double availableFund;
    private double pendingFund;


    public double getAvailableFund() {
        return availableFund;
    }

    public double getPendingFund() {
        return pendingFund;
    }

    public void depositFund(double amount) throws IllegalArgumentException{
        if(amount>0)
            this.availableFund += amount;
        else
            throw new IllegalArgumentException("Invalid amount");
    }

    public String qualifyCandidate(Candidate candidate) {
        //qualified case

        if(candidate.getDebtToIncome()<Constants.MIN_DTI
                && candidate.getCreditScore()>=Constants.MIN_CREDITSCORE){
            if( candidate.getSavings()>= candidate.getRequestedAmount()*Constants.MIN_SAVING_RATIO){
                candidate.setQualification(Constants.QUALIFIED);
                candidate.setLoan_amount(candidate.getRequestedAmount());
            }
            else {
                candidate.setQualification(Constants.PARTIALLY_QUALIFIED);
                candidate.setLoan_amount(candidate.getSavings()*4);
            }
            candidate.setStatus(Constants.QUALIFIED);

        }else {         //denied case
            candidate.setQualification(Constants.NOT_QUALIFIED);
            candidate.setLoan_amount(0);
            candidate.setStatus(Constants.DENIED);
        }
        return candidate.getStatus();
    }

    public String approveLoan(Candidate candidate) throws Exception {
        if(Constants.DENIED.equalsIgnoreCase(candidate.getStatus()))
            throw new Exception("Cannot proceed");
        if(availableFund >=candidate.getLoan_amount()){
            candidate.setStatus(Constants.APPROVED);
            pendingFund += candidate.getLoan_amount();
            availableFund -= candidate.getLoan_amount();
        }
        else {
            candidate.setStatus(Constants.ON_HOLD);
        }
        return candidate.getStatus();
    }

    public void processOffer(Candidate candidate) {
        if(candidate.isAccept()) {
            pendingFund -= candidate.getLoan_amount();
            candidate.setStatus(Constants.ACCEPTED);
        }
        else {
            availableFund += candidate.getLoan_amount();
            pendingFund -= candidate.getLoan_amount();
            candidate.setStatus(Constants.REJECTED);
        }
    }
}
