package com.zopa.lending;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.zopa.lending.market.Lender;

class LoanQuoteTest {

  @Test
  void testGetRequestedAmount() {
    int loanAmount = 1000;
    int loanLengthInMonths = 10;
    LoanQuote quote = new LoanQuote(loanAmount, loanLengthInMonths);
    assertEquals(1000, quote.getRequestedAmount());
  }

  @Test
  void testGetTotalRepayment() {
    int loanAmount = 1000;
    int loanLengthInMonths = 10;
    LoanQuote quote = new LoanQuote(loanAmount, loanLengthInMonths);
    quote.borrow(new Lender("Arturo", 0.1, 1000));
    assertEquals(1100, quote.getTotalRepayment());
  }

  @Test
  void testGetRate_SameAmountMultipleLenders() {
    int loanAmount = 1000;
    int loanLengthInMonths = 10;
    LoanQuote quote = new LoanQuote(loanAmount, loanLengthInMonths);
    quote.borrow(new Lender("Arturo", 0.06, 500));
    quote.borrow(new Lender("Raquel", 0.02, 500));
    assertEquals(0.04, quote.getRate());
  }
  
  @Test
  void testGetRate_DifferentAmountMultipleLenders() {
    int loanAmount = 1000;
    int loanLengthInMonths = 10;
    LoanQuote quote = new LoanQuote(loanAmount, loanLengthInMonths);
    quote.borrow(new Lender("Arturo", 0.06, 800));
    quote.borrow(new Lender("Raquel", 0.02, 200));
    assertEquals(0.052, quote.getRate());
  }
  
  @Test
  void testGetRate_SingleLender() {
    int loanAmount = 1000;
    int loanLengthInMonths = 10;
    LoanQuote quote = new LoanQuote(loanAmount, loanLengthInMonths);
    quote.borrow(new Lender("Arturo", 0.06, 1000));
    assertEquals(0.06, quote.getRate());
  }

  @Test
  void testGetMonthlyRepayment() {
    int loanAmount = 1000;
    int loanLengthInMonths = 10;
    LoanQuote quote = new LoanQuote(loanAmount, loanLengthInMonths);
    quote.borrow(new Lender("Arturo", 0.1, 1000));
    assertEquals(110, quote.getMonthlyRepayment());
  }

  @Test
  void testIsSatisfied_NotEnoughFunds() {
    int loanAmount = 1000;
    int loanLengthInMonths = 10;
    LoanQuote quote = new LoanQuote(loanAmount, loanLengthInMonths);
    Lender lender1 = new Lender("Arturo", 0.06, 450);
    quote.borrow(lender1);
    assertFalse(quote.isSatisfied());
  }
  
  @Test
  void testIsSatisfied_EnoughFunds() {
    int loanAmount = 1000;
    int loanLengthInMonths = 10;
    LoanQuote quote = new LoanQuote(loanAmount, loanLengthInMonths);
    Lender lender1 = new Lender("Arturo", 0.06, 1500);
    quote.borrow(lender1);
    assertTrue(quote.isSatisfied());
  }

  @Test
  void testBorrow() {
    int loanAmount = 1000;
    int loanLengthInMonths = 10;
    LoanQuote quote = new LoanQuote(loanAmount, loanLengthInMonths);
    int remainingUnborrowed = quote.borrow(new Lender("Arturo", 0.06, 450));
    assertEquals(550, remainingUnborrowed);
    remainingUnborrowed = quote.borrow(new Lender("Raquel", 0.07, 10000));
    assertEquals(0, remainingUnborrowed);
  }

}
