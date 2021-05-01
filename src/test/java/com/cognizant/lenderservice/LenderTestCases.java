package com.cognizant.lenderservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LenderTestCases {
    private Lender lender;
    @BeforeEach
    public void setUp(){
        lender = new Lender();
    }

    @Test
    public void testEmptyFund(){
        double fund = lender.getAvailableFund();
        assertEquals(0, fund);
    }

    @Test
    public void testAvailableFund(){
        double initFund = 1000000;
        double addingAmount = 500000;

        lender.depositFund(initFund);
        double fund = lender.getAvailableFund();
        assertEquals(initFund, lender.getAvailableFund());

        lender.depositFund(addingAmount);
        fund = lender.getAvailableFund();
        assertEquals(initFund + addingAmount, lender.getAvailableFund());
    }

    @Test
    public void testInvalidDepositFund(){
        double initFund = -1000000;
        assertThrows(IllegalArgumentException.class, ()-> lender.depositFund(initFund) );
    }
    // QUALIFY CANDIDATE
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
    //APPROVE LOAN
    @Test
    public void testApproveLoanWithLessFund()  throws Exception{
        Candidate candidate = new Candidate(700, 21, 100000, 250000 );
        String status = lender.qualifyCandidate(candidate);
        assertEquals(Constants.QUALIFIED, status);
        String approvedStatus = lender.approveLoan(candidate);
        assertTrue(candidate.getLoan_amount()> lender.getAvailableFund());
        assertEquals(Constants.ON_HOLD, approvedStatus);
    }

    @Test
    public void testApproveLoanWithMoreFund() throws Exception {
        Candidate candidate = new Candidate(700, 21, 100000, 250000 );
        String status = lender.qualifyCandidate(candidate);
        assertEquals(Constants.QUALIFIED, status);
        lender.depositFund(500000);
        String approvedStatus = lender.approveLoan(candidate);
        assertTrue(candidate.getLoan_amount()< lender.getAvailableFund());
        assertEquals(Constants.APPROVED, approvedStatus);
    }
    @Test
    public void testApproveLoanWithEqualsFund()  throws Exception{
        Candidate candidate = new Candidate(700, 21, 100000, 250000 );
        String status = lender.qualifyCandidate(candidate);
        assertEquals(Constants.QUALIFIED, status);
        lender.depositFund(250000);
        String approvedStatus = lender.approveLoan(candidate);
        assertEquals(candidate.getLoan_amount(), lender.getAvailableFund());
        assertEquals(Constants.APPROVED, approvedStatus);
    }
    @Test
    public void testApproveLoanForDeniedCandidate(){
        Candidate candidate = new Candidate(700, 37, 100000, 250000 );
        String status = lender.qualifyCandidate(candidate);
        assertEquals(Constants.NOT_QUALIFIED, candidate.getQualification());
        assertEquals(Constants.DENIED, status);
        lender.depositFund(500000);
        assertThrows(Exception.class, () -> lender.approveLoan(candidate));
    }

    @Test
    public void testPendingFundForApprovedLoan() throws Exception {
        Candidate candidate = new Candidate(700, 21, 100000, 250000 );
        String status = lender.qualifyCandidate(candidate);
        assertEquals(Constants.QUALIFIED, status);
        lender.depositFund(1000000);
        double expectedPendingFund=  candidate.getLoan_amount();
        double expectedAvailableFund= lender.getAvailableFund()- candidate.getLoan_amount();
        String approvedStatus = lender.approveLoan(candidate);
        assertEquals(Constants.APPROVED, approvedStatus);
        assertEquals(expectedPendingFund, lender.getPendingFund());
        assertEquals(expectedAvailableFund, lender.getAvailableFund());

        Candidate candidate2 = new Candidate(700, 30, 50000, 250000 );
        lender.qualifyCandidate(candidate);
        expectedPendingFund +=  candidate2.getLoan_amount();
        expectedAvailableFund -= candidate2.getLoan_amount();
        approvedStatus = lender.approveLoan(candidate2);
        assertEquals(Constants.APPROVED, approvedStatus);
        assertEquals(expectedPendingFund, lender.getPendingFund());
        assertEquals(expectedAvailableFund, lender.getAvailableFund());
    }
    @Test
    public void testPendingFundForOnHoldLoan() throws Exception {
        Candidate candidate = new Candidate(700, 21, 100000, 250000 );
        String status = lender.qualifyCandidate(candidate);
        lender.depositFund(100000);

        String approvedStatus = lender.approveLoan(candidate);
        assertEquals(Constants.ON_HOLD, approvedStatus);
        assertEquals(0, lender.getPendingFund());
        assertEquals(100000, lender.getAvailableFund());

    }
    @Test
    public void testAcceptOffer() throws Exception {
        Candidate candidate = new Candidate(700, 21, 100000, 250000 );
        String status = lender.qualifyCandidate(candidate);
        lender.depositFund(1000000);
        lender.approveLoan(candidate);
        double pendingFundBeforeAcceptOffer= lender.getPendingFund();
        candidate.acceptOrRejectOffer(true);
        lender.processOffer( candidate);
        assertTrue(candidate.isAccept());
        assertEquals(0, lender.getPendingFund());
        assertEquals(750000, lender.getAvailableFund());
        assertEquals(Constants.ACCEPTED, candidate.getStatus());
    }

    @Test
    public void testRejectOffer() throws Exception {
        Candidate candidate = new Candidate(700, 21, 100000, 250000 );
        String status = lender.qualifyCandidate(candidate);
        lender.depositFund(1000000);
        lender.approveLoan(candidate);
        double pendingFundBeforeAcceptOffer= lender.getPendingFund();
        candidate.acceptOrRejectOffer(false);
        lender.processOffer(candidate);
        assertFalse(candidate.isAccept());
        assertEquals(0, lender.getPendingFund());
        assertEquals(1000000, lender.getAvailableFund());
        assertEquals(Constants.REJECTED, candidate.getStatus());
    }
}
