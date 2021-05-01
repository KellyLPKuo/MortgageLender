package com.cognizant.lenderservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LenderTestCases {
    private Lender lender;
    private Loaner loaner;
    @BeforeEach
    public void setUp(){
        lender = new Lender();
        loaner = new Loaner("Karson", "Johnson", "123-34-6734");
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
        CandidateLoan candidateLoan = new CandidateLoan(700, 21, 100000, 250000 );
        String status = lender.qualifyCandidate(loaner, candidateLoan);
        assertEquals(Constants.QUALIFIED, candidateLoan.getQualification());
        assertEquals(Constants.QUALIFIED, status);
        assertEquals(250000, candidateLoan.getLoan_amount());
    }

    @Test
    public void testDeniedHighDTI(){
        CandidateLoan candidateLoan = new CandidateLoan(700, 37, 100000, 250000 );
        String status = lender.qualifyCandidate(loaner, candidateLoan);
        assertEquals(Constants.NOT_QUALIFIED, candidateLoan.getQualification());
        assertEquals(Constants.DENIED, status);
        assertEquals(0, candidateLoan.getLoan_amount());
    }

    @Test
    public void testDeniedBadCreditScore(){
        CandidateLoan candidateLoan = new CandidateLoan(600, 30, 100000, 250000 );
        String status = lender.qualifyCandidate(loaner, candidateLoan);
        assertEquals(Constants.NOT_QUALIFIED, candidateLoan.getQualification());
        assertEquals(Constants.DENIED, status);
        assertEquals(0, candidateLoan.getLoan_amount());

    }
    @Test
    public void testPartialQualification(){
        CandidateLoan candidateLoan = new CandidateLoan(700, 30, 50000, 250000 );
        String status = lender.qualifyCandidate(loaner, candidateLoan);
        assertEquals(Constants.PARTIALLY_QUALIFIED, candidateLoan.getQualification());
        assertEquals(Constants.QUALIFIED, status);
        assertEquals(200000, candidateLoan.getLoan_amount());
    }
    //APPROVE LOAN
    @Test
    public void testApproveLoanWithLessFund()  throws Exception{
        CandidateLoan candidateLoan = new CandidateLoan(700, 21, 100000, 250000 );
        String status = lender.qualifyCandidate(loaner, candidateLoan);
        assertEquals(Constants.QUALIFIED, status);
        String approvedStatus = lender.approveLoan(loaner, candidateLoan);
        assertTrue(candidateLoan.getLoan_amount()> lender.getAvailableFund());
        assertEquals(Constants.ON_HOLD, approvedStatus);
    }

    @Test
    public void testApproveLoanWithMoreFund() throws Exception {
        CandidateLoan candidateLoan = new CandidateLoan(700, 21, 100000, 250000 );
        String status = lender.qualifyCandidate(loaner, candidateLoan);
        assertEquals(Constants.QUALIFIED, status);
        lender.depositFund(600000);
        String approvedStatus = lender.approveLoan(loaner, candidateLoan);
        assertTrue( lender.getAvailableFund()>0);
        assertEquals(Constants.APPROVED, approvedStatus);
    }
    @Test
    public void testApproveLoanWithEqualsFund()  throws Exception{
        CandidateLoan candidateLoan = new CandidateLoan(700, 21, 100000, 250000 );
        String status = lender.qualifyCandidate(loaner, candidateLoan);
        assertEquals(Constants.QUALIFIED, status);
        lender.depositFund(250000);
        assertEquals(candidateLoan.getLoan_amount(), lender.getAvailableFund());

        String approvedStatus = lender.approveLoan(loaner, candidateLoan);
        assertEquals(Constants.APPROVED, approvedStatus);
        assertEquals(0, lender.getAvailableFund());
    }
    @Test
    public void testApproveLoanForDeniedCandidate(){
        CandidateLoan candidateLoan = new CandidateLoan(700, 37, 100000, 250000 );
        String status = lender.qualifyCandidate(loaner, candidateLoan);
        assertEquals(Constants.NOT_QUALIFIED, candidateLoan.getQualification());
        assertEquals(Constants.DENIED, status);
        lender.depositFund(500000);
        assertThrows(Exception.class, () -> lender.approveLoan(loaner, candidateLoan));
    }

    @Test
    public void testPendingFundForApprovedLoan() throws Exception {
        CandidateLoan candidateLoan = new CandidateLoan(700, 21, 100000, 250000 );
        String status = lender.qualifyCandidate(loaner, candidateLoan);
        assertEquals(Constants.QUALIFIED, status);
        lender.depositFund(1000000);
        double expectedPendingFund=  candidateLoan.getLoan_amount();
        double expectedAvailableFund= lender.getAvailableFund()- candidateLoan.getLoan_amount();
        String approvedStatus = lender.approveLoan(loaner, candidateLoan);
        assertEquals(Constants.APPROVED, approvedStatus);
        assertEquals(expectedPendingFund, lender.getPendingFund());
        assertEquals(expectedAvailableFund, lender.getAvailableFund());

        CandidateLoan candidateLoan2 = new CandidateLoan(700, 30, 50000, 250000 );
        lender.qualifyCandidate(loaner,candidateLoan);
        expectedPendingFund +=  candidateLoan2.getLoan_amount();
        expectedAvailableFund -= candidateLoan2.getLoan_amount();

        approvedStatus = lender.approveLoan(loaner, candidateLoan2);
        assertEquals(Constants.APPROVED, approvedStatus);
        assertEquals(expectedPendingFund, lender.getPendingFund());
        assertEquals(expectedAvailableFund, lender.getAvailableFund());
    }
    @Test
    public void testPendingFundForOnHoldLoan() throws Exception {
        CandidateLoan candidateLoan = new CandidateLoan(700, 21, 100000, 250000 );
        String status = lender.qualifyCandidate(loaner, candidateLoan);
        lender.depositFund(100000);

        String approvedStatus = lender.approveLoan(loaner, candidateLoan);
        assertEquals(Constants.ON_HOLD, approvedStatus);
        assertEquals(0, lender.getPendingFund());
        assertEquals(100000, lender.getAvailableFund());

    }
    @Test
    public void testAcceptOffer() throws Exception {
        CandidateLoan candidateLoan = new CandidateLoan(700, 21, 100000, 250000 );
        String status = lender.qualifyCandidate(loaner, candidateLoan);
        lender.depositFund(1000000);
        lender.approveLoan(loaner, candidateLoan);
        double pendingFundBeforeAcceptOffer= lender.getPendingFund();
        candidateLoan.acceptOrRejectOffer(true);
        lender.processOffer(loaner, candidateLoan);
        assertTrue(candidateLoan.isAccept());
        assertEquals(0, lender.getPendingFund());
        assertEquals(750000, lender.getAvailableFund());
        assertEquals(Constants.ACCEPTED, candidateLoan.getStatus());
    }

    @Test
    public void testRejectOffer() throws Exception {
        CandidateLoan candidateLoan = new CandidateLoan(700, 21, 100000, 250000 );
        String status = lender.qualifyCandidate(loaner, candidateLoan);
        lender.depositFund(1000000);
        lender.approveLoan(loaner, candidateLoan);
        double pendingFundBeforeAcceptOffer= lender.getPendingFund();
        candidateLoan.acceptOrRejectOffer(false);
        lender.processOffer(loaner, candidateLoan);
        assertFalse(candidateLoan.isAccept());
        assertEquals(0, lender.getPendingFund());
        assertEquals(1000000, lender.getAvailableFund());
        assertEquals(Constants.REJECTED, candidateLoan.getStatus());
    }
    @Test
    public void testUndecidedLoans()  throws Exception{
        CandidateLoan candidateLoan1 = new CandidateLoan(700, 21, 100000, 250000 );
        String status = lender.qualifyCandidate(loaner, candidateLoan1);
        lender.depositFund(1000000);
        lender.approveLoan(loaner, candidateLoan1);
        assertEquals(LocalDate.now().plusDays(3), candidateLoan1.getExpireDate());
        assertFalse(candidateLoan1.isDecided());

        CandidateLoan candidateLoan2 = new CandidateLoan(700, 30, 50000, 250000 );
        lender.qualifyCandidate(loaner, candidateLoan1);

        lender.approveLoan(loaner, candidateLoan2);
        candidateLoan2.acceptOrRejectOffer(true);
        boolean expired = candidateLoan2.getExpireDate().compareTo(LocalDate.now())>0
                && candidateLoan2.getExpireDate().compareTo(LocalDate.now())<=3
                && candidateLoan2.isDecided()==false;
       // assertTrue(expired);
        assertFalse(candidateLoan1.isDecided());

        List<CandidateLoan> unDecidedCandidates = lender.getUnDecidedLoan();
        assertEquals(candidateLoan1, unDecidedCandidates.get(0));
        assertEquals(1, unDecidedCandidates.size());
    }
}
