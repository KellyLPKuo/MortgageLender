package com.cognizant.lenderservice;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Lender {
    private double availableFund;
    private double pendingFund;
    private Map<String, Loaner> loaners = new HashMap<>();

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

    public String qualifyCandidate(Loaner loaner, CandidateLoan candidateLoan) {
        //qualified case
        if(!loaners.containsKey(loaner.getSSN())){
            loaners.put(loaner.getSSN(), loaner);
        }

        if(!loaner.getCandidateLoans().contains(candidateLoan)){
            loaner.getCandidateLoans().add(candidateLoan);
        }
        else{
            CandidateLoan temp =
                    loaner.getCandidateLoans()
                    .stream()
                    .filter(c-> c.equals(candidateLoan)).findAny()
                    .get();

            temp = candidateLoan;
        }
        if (candidateLoan.getDebtToIncome() < Constants.MIN_DTI
                && candidateLoan.getCreditScore() >= Constants.MIN_CREDITSCORE) {
            if (candidateLoan.getSavings() >= candidateLoan.getRequestedAmount() * Constants.MIN_SAVING_RATIO) {
                candidateLoan.setQualification(Constants.QUALIFIED);
                candidateLoan.setLoan_amount(candidateLoan.getRequestedAmount());
            } else {
                candidateLoan.setQualification(Constants.PARTIALLY_QUALIFIED);
                candidateLoan.setLoan_amount(candidateLoan.getSavings() * 4);
            }
            candidateLoan.setStatus(Constants.QUALIFIED);

        } else {         //denied case
            candidateLoan.setQualification(Constants.NOT_QUALIFIED);
            candidateLoan.setLoan_amount(0);
            candidateLoan.setStatus(Constants.DENIED);
        }

        return candidateLoan.getStatus();
    }

    public String approveLoan(Loaner loaner, CandidateLoan candidateLoan) throws Exception {
        if(loaners.containsKey(loaner.getSSN())) {
            loaner =  loaners.get(loaner.getSSN());
            CandidateLoan temp =
                    loaner.getCandidateLoans()
                            .stream()
                            .filter(c-> c.equals(candidateLoan)).findFirst()
                            .get();

            temp = candidateLoan;
            if (Constants.DENIED.equalsIgnoreCase(candidateLoan.getStatus()))
                throw new Exception("Cannot proceed");
            if (availableFund >= candidateLoan.getLoan_amount()) {
                candidateLoan.setStatus(Constants.APPROVED);
                pendingFund += candidateLoan.getLoan_amount();
                availableFund -= candidateLoan.getLoan_amount();
                candidateLoan.setExpireDate(LocalDate.now().plusDays(3));
            } else {
                candidateLoan.setStatus(Constants.ON_HOLD);
            }
        }
        return candidateLoan.getStatus();
    }

    public void processOffer(Loaner loaner, CandidateLoan candidateLoan) {
        if(loaners.containsKey(loaner.getSSN())) {
            loaner = loaners.get(loaner.getSSN());
            CandidateLoan temp =
                    loaner.getCandidateLoans()
                            .stream()
                            .filter(c -> c.equals(candidateLoan)).findAny()
                            .get();

            temp = candidateLoan;
            if (candidateLoan.isAccept()) {
                pendingFund -= candidateLoan.getLoan_amount();
                candidateLoan.setStatus(Constants.ACCEPTED);
            } else {
                availableFund += candidateLoan.getLoan_amount();
                pendingFund -= candidateLoan.getLoan_amount();
                candidateLoan.setStatus(Constants.REJECTED);
            }
        }
    }

    public List<CandidateLoan> getUnDecidedLoan() {
        List<CandidateLoan> unDecidedLoan =  new ArrayList<>();
        for(Loaner loaner: loaners.values()){
            for(CandidateLoan c: loaner.getCandidateLoans()){
                if(c.getExpireDate().compareTo(LocalDate.now())>0
                        && c.getExpireDate().compareTo(LocalDate.now())<=3
                        && c.isDecided()==false){
                    unDecidedLoan.add(c);
                }
            }

        }
        return unDecidedLoan;
//        return candidateLoans.stream()
//                .filter( c-
//                )
//                .peek(c-> {
//                    System.out.println(c);
//                    System.out.println(c.getExpireDate().compareTo(LocalDate.now()));
//                    System.out.println(c.isDecided());
//
//                })
//                .collect(Collectors.toList());

    }
}
