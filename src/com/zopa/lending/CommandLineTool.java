package com.zopa.lending;

import com.zopa.lending.market.LocalMarketFile;
import com.zopa.lending.market.MarketData;

/** Main class to read parameters from Command Line and return the result. */
public class CommandLineTool {
  
  /** Default loan length in months. */
  private static final int DEFAULT_LOAN_LENGTH = 36;
	
	/** Number of arguments needed to run this tool. */
	private static final int NUMBER_OF_MANDATORY_ARGUMENTS = 3;
	
	/** Total number of arguments including optional arguments. */
	private static final int TOTAL_NUMBER_OF_ARGUMENTS = 
	    NUMBER_OF_MANDATORY_ARGUMENTS + 1; // Optional loan length parameter.
	
	/** Position of the market file argument. */
	private static final int MARKET_FILE_ARGUMENT = 1;
	
	/** Position of the loan amount argument. */
	private static final int LOAN_AMOUNT_ARGUMENT = 2;
	
	/** Position of the loan length argument. */
	private static final int LOAN_LENGTH_ARGUMENT = 3;

	/** Exit status for wrong argument type. */
	private static final int WRONG_ARGUMENT_TYPE_STATUS = -1;

	/** Exit status for market related errors. */
	private static final int MARKET_ERROR_STATUS = -2;

	private static final int INCORRECT_LOAN_AMOUNT_STATUS = -3;
  
	
	private CommandLineTool() {
		// Restricting visibility of default constructor to prevent unwanted instantiations.
	}

	public static void main(String[] args) {
		if (args.length != NUMBER_OF_MANDATORY_ARGUMENTS) {
			System.out.println(
			    "Usage: java -jar LoanQuote.jar [market_file] [loan_amount] [optional loan_length]");
		}
		
		String marketFileName = args[MARKET_FILE_ARGUMENT];
		MarketData marketData = new LocalMarketFile(marketFileName);
		
    int loanAmount = 0;
    try {
      loanAmount = Integer.parseInt(args[LOAN_AMOUNT_ARGUMENT]);
    } catch (NumberFormatException e) {
      System.out.println("Error message: Loan amount is not a number.");
      System.exit(WRONG_ARGUMENT_TYPE_STATUS);
    }

    int loanLengthInMonths = 0;
    try {
      loanLengthInMonths = (args.length == TOTAL_NUMBER_OF_ARGUMENTS) ? 
          Integer.parseInt(args[LOAN_LENGTH_ARGUMENT]) : DEFAULT_LOAN_LENGTH;
    } catch (NumberFormatException e) {
      System.out.println("Error message: Loan length in months is not a number.");
      System.exit(WRONG_ARGUMENT_TYPE_STATUS);
    }

    QuoteEngine quoteEngine = new QuoteEngine(marketData);
    try {
      LoanQuote quote = quoteEngine.getQuote(loanAmount, loanLengthInMonths);
      printLoanQuote(quote);
    } catch (LoanAmountOutOfRangeException | IncorrectLoanAmountException e) {
      System.out.println("Error message: " + e.getMessage());
      System.exit(INCORRECT_LOAN_AMOUNT_STATUS);
    } catch (NoLendersException | NotEnoughMarketFundsException e) {
      System.out.println("Error message: " + e.getMessage());
      System.exit(MARKET_ERROR_STATUS);
    }

    System.exit(0);
  }
	
	private static void printLoanQuote(LoanQuote quote) {
	  
	}

}
