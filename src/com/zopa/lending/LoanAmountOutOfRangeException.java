package com.zopa.lending;

public class LoanAmountOutOfRangeException extends Exception {
	
	private static final long serialVersionUID = 3235165367934889572L;
	
	private static final String MESSAGE = "Error: Loan amount must be between £%s and £%s";
	
	/** Constructor with customizable min and max loan amounts. */
	LoanAmountOutOfRangeException(int minLoanAmount, int maxLoanAmount) {
		super(String.format(MESSAGE, minLoanAmount, maxLoanAmount));
	}

}
