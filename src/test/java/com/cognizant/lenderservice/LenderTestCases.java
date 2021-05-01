package com.cognizant.lenderservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LenderTestCases {
    private Lender lender;
    @BeforeEach
    public void setUp(){
        lender = new Lender();
    }

    @Test
    public void testEmptyFund(){
        double fund = lender.getCurrentFund();
        assertEquals(0, fund);
    }

    @Test
    public void testAvailableFund(){
        double initFund = 1000000;
        double addingAmount = 500000;

        lender.depositFund(initFund);
        double fund = lender.getCurrentFund();
        assertEquals(initFund, lender.getCurrentFund());

        lender.depositFund(addingAmount);
        fund = lender.getCurrentFund();
        assertEquals(initFund + addingAmount, lender.getCurrentFund());
    }

    @Test
    public void testInvalidDepositFund(){
        double initFund = -1000000;
        assertThrows(IllegalArgumentException.class, ()-> lender.depositFund(initFund) );
    }

    @Test
    public void testQualifyApplication(){
        Candidate candidate = new Candidate(700, 21, 100000, 250000 );
        String status = lender.qualifyCandidate(candidate);
        assertEquals(Constants.QUALIFIED, candidate.getQualification());
        assertEquals(Constants.QUALIFIED, status);
        assertEquals(250000, candidate.getLoan_amount());
    }

    @Test
    public void testDeniedHighDTI(){
        Candidate candidate = new Candidate(700, 37, 100000, 250000 );
        String status = lender.qualifyCandidate(candidate);
        assertEquals(Constants.NOT_QUALIFIED, candidate.getQualification());
        assertEquals(Constants.DENIED, status);
        assertEquals(0, candidate.getLoan_amount());
    }

    @Test
    public void testDeniedBadCreditScore(){
        Candidate candidate = new Candidate(600, 30, 100000, 250000 );
        String status = lender.qualifyCandidate(candidate);
        assertEquals(Constants.NOT_QUALIFIED, candidate.getQualification());

        assertEquals(Constants.DENIED, status);
        assertEquals(0, candidate.getLoan_amount());

    }
    @Test
    public void testPartialQualification(){
        Candidate candidate = new Candidate(700, 30, 50000, 250000 );
        String status = lender.qualifyCandidate(candidate);
        assertEquals(Constants.PARTIALLY_QUALIFIED, candidate.getQualification());

        assertEquals(Constants.QUALIFIED, status);
        assertEquals(200000, candidate.getLoan_amount());

    }
}
