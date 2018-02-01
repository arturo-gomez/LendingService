package com.zopa.lending;

import java.util.ArrayList;
import java.util.List;

import com.zopa.lending.market.Lender;

/**
 * Tracks the breakdown of lenders for a Loan together with other key figures
 * for a loan quote.
 */
public class LoanQuote {

  private int remainingAmount;
  private final int requestedAmount;
  private final List<LoanLineItem> loanBreakdown;
  private final int loanLengthInMonths;

  LoanQuote(int loanAmount, int loanLengthInMonths) {
    remainingAmount = loanAmount;
    requestedAmount = loanAmount;
    this.loanLengthInMonths = loanLengthInMonths;
    loanBreakdown = new ArrayList<LoanLineItem>();
  }

  /** Returns the total amount requested by the borrower. */
  int getRequestedAmount() {
    return requestedAmount;
  }

  /** Returns the total amount to be paid by the borrower. */
  double getTotalRepayment() {
    double rate = 1 + getRate();
    return requestedAmount * rate;
  }

  /** Returns the overall interest rate to be paid for this quote. */
  double getRate() {
    double weightedSum = loanBreakdown.stream()
        .mapToDouble(item -> item.getRate() * item.getAmount())
        .sum();
    return weightedSum / requestedAmount;
  }

  /** Returns the monthly re-payment for this quote. */
  double getMonthlyRepayment() {
    double totalRepayment = getTotalRepayment();
    return totalRepayment / loanLengthInMonths;
  }

  /** Returns true if the amount requested has been borrowed already. */
  boolean isSatisfied() {
    return remainingAmount == 0;
  }

  /**
   * Borrows money from this {@code lender}. If this lender has not enough money
   * available, it will borrow all and update the remaining amount needed.
   * 
   * @return the amount that is not yet borrowed from the loan amount.
   */
  int borrow(Lender lender) {
    int amountAvailable = lender.getAmount();
    int amount = (amountAvailable > remainingAmount) ? remainingAmount : amountAvailable;
    loanBreakdown.add(new LoanLineItem(amount, lender.getRate()));
    remainingAmount -= amount;
    return remainingAmount;
  }

  /** Internal class to track the loan breakdown by lender. */
  private static class LoanLineItem {
    private final int amount;
    private final double rate;

    private LoanLineItem(int amount, double rate) {
      this.amount = amount;
      this.rate = rate;
    }

    public int getAmount() {
      return amount;
    }

    public double getRate() {
      return rate;
    }
  }

}
