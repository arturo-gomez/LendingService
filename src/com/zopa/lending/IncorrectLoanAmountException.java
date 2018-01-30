package com.zopa.lending;

public class IncorrectLoanAmountException extends Exception {
	
	private static final long serialVersionUID = -246825528394756673L;
	
	private static final String MESSAGE = "Error: Incorrect loan amout must be set in £%s increments.";
	
	/** Constructor with customizable loan amount increments. */
	IncorrectLoanAmountException(int loanAmountIncrement) {
		super(String.format(MESSAGE, loanAmountIncrement));
	}

}
