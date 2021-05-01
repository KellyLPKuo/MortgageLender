package com.cognizant.lenderservice;

import java.util.ArrayList;
import java.util.List;

public class Loaner {
    private String firstName;
    private String lastName;
    private String SSN;

    List<CandidateLoan> candidateLoans= new ArrayList<>();
    public Loaner(String firstName, String lastName, String SSN) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.SSN = SSN;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSSN() {
        return SSN;
    }

    public void setSSN(String SSN) {
        this.SSN = SSN;
    }

    public List<CandidateLoan> getCandidateLoans() {
        return candidateLoans;
    }

    public void setCandidateLoans(List<CandidateLoan> candidateLoans) {
        this.candidateLoans = candidateLoans;
    }

    @Override
    public String toString() {
        return "Loaner{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", SSN='" + SSN + '\'' +
                '}';
    }
}
