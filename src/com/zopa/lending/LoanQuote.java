package com.zopa.lending;

import java.util.ArrayList;
import java.util.List;

import com.zopa.lending.market.Lender;

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
	
	double getRate() {
	  return -1;
	}
	
	int getRequestedAmount() {
	  return requestedAmount;
	}
	
	double getMonthlyRepayment() {
	  return -1.0;
	}
	
	double getTotalRepayment() {
	  return -1.0;
	}
	
	boolean isSatisfied() {
		return remainingAmount == 0;
	}

	void borrowFrom(Lender lender) {
		int amountAvailable = lender.getAmount();
		int amount = (amountAvailable > remainingAmount) ? remainingAmount : amountAvailable;
		loanBreakdown.add(new LoanLineItem(amount, lender.getRate()));
		remainingAmount -= amount;
	}
	
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
