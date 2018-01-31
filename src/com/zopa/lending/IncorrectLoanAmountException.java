package com.zopa.lending;

/**
 * Exception to be thrown when the amount requested is not set in the increments
 * expected.
 */
public class IncorrectLoanAmountException extends Exception {

  private static final long serialVersionUID = -246825528394756673L;

  private static final String MESSAGE = "Incorrect loan amout. It must be set in £%s increments.";

  /** Constructor with customizable loan amount increments. */
  IncorrectLoanAmountException(int loanAmountIncrement) {
    super(String.format(MESSAGE, loanAmountIncrement));
  }

}
