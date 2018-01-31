package com.zopa.lending;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.NumberFormat;
import java.util.Locale;

import com.zopa.lending.market.CsvMarketData;
import com.zopa.lending.market.MarketData;
import com.zopa.lending.market.MarketDataException;

/** Main class to read parameters from Command Line and print the loan quote. */
public class CommandLineTool {

  /** Default loan length in months. */
  private static final int DEFAULT_LOAN_LENGTH = 36;

  /** Number of arguments needed to run this tool. */
  private static final int NUMBER_OF_MANDATORY_ARGUMENTS = 2;

  /** Total number of arguments including optional arguments. */
  private static final int TOTAL_NUMBER_OF_ARGUMENTS = NUMBER_OF_MANDATORY_ARGUMENTS + 1; 

  /** Position of the market file argument. */
  private static final int MARKET_FILE_ARGUMENT = 0;

  /** Position of the loan amount argument. */
  private static final int LOAN_AMOUNT_ARGUMENT = 1;

  /** Position of the loan length argument. */
  private static final int LOAN_LENGTH_ARGUMENT = 2;

  /** Exit status for wrong argument type. */
  private static final int WRONG_ARGUMENT_TYPE_STATUS = -1;

  /** Exit status for market related errors. */
  private static final int MARKET_DATA_ERROR_STATUS = -2;

  /** Exit status for loan amount errors. */
  private static final int INCORRECT_LOAN_AMOUNT_STATUS = -3;
  
  /** Exit status to be used when required arguments are missing. */
  private static final int MISSING_REQUIRED_ARGUMENTS_STATUS = -4;

  private CommandLineTool() {
    // Restricting visibility of default constructor to prevent unwanted
    // instantiations.
  }

  /**
   * Checks whether the parameters are correct and request the loan quote.
   * 
   * Where the magic begins ...
   */
  public static void main(String[] args) {
    if (args.length != NUMBER_OF_MANDATORY_ARGUMENTS) {
      System.out.println(
          "Usage: java -jar LoanQuote.jar [market_file] [loan_amount] [optional loan_length]");
      System.exit(MISSING_REQUIRED_ARGUMENTS_STATUS);
    }

    String marketFileName = args[MARKET_FILE_ARGUMENT];
    FileReader reader = null;
    try {
      reader = new FileReader(new File(marketFileName));
    } catch (FileNotFoundException e1) {
      System.out.println("Error: Market data CSV file not found.");
      System.exit(MARKET_DATA_ERROR_STATUS);
    }
    MarketData marketData = new CsvMarketData(reader);

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
    } catch (MarketDataException | NoLendersException | NotEnoughMarketFundsException e) {
      System.out.println("Error message: " + e.getMessage());
      System.exit(MARKET_DATA_ERROR_STATUS);
    }

    System.exit(0);
  }

  /** Prints the quote. */
  private static void printLoanQuote(LoanQuote quote) {
    NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.UK);
    NumberFormat percentageFormatter = NumberFormat.getPercentInstance();
    System.out.println(
        String.format("Requested amount: %s", formatter.format(quote.getRequestedAmount())));
    System.out.println(
        String.format("Rate: %s", percentageFormatter.format(quote.getRate())));
    System.out.println(
        String.format("Monthly repayment: %s", formatter.format(quote.getMonthlyRepayment())));
    System.out.println(
        String.format("Total repayment: %s", formatter.format(quote.getTotalRepayment())));
  }

}
