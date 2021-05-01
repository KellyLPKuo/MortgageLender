package com.cognizant.lenderservice;

public class Lender {
    private double fund;


    public double getCurrentFund() {
        return fund;
    }

    public void depositFund(double amount) throws IllegalArgumentException{
        if(amount>0)
            this.fund += amount;
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
            candidate.setStatus(true);

        }else{         //denied case
            candidate.setQualification(Constants.NOT_QUALIFIED);
            candidate.setLoan_amount(0);
            candidate.setStatus(false);
        }
        return checkStatus(candidate.getStatus());
    }
    private String checkStatus(boolean status){
        if(status) return Constants.QUALIFIED;
        return Constants.DENIED;
    }

}
