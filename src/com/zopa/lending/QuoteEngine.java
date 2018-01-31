package com.zopa.lending;

import java.util.List;
import java.util.stream.Collectors;

import com.zopa.lending.market.Lender;
import com.zopa.lending.market.MarketData;
import com.zopa.lending.market.MarketDataException;

/**
 * Business logic to calculate a loan quote with the lowest interest rate based
 * on the {@link MarketData} provided.
 */
public class QuoteEngine {

  private static final int MIN_LOAN_AMOUNT = 1000;
  private static final int MAX_LOAN_AMOUNT = 15000;
  private static final int LOAN_AMOUNT_INCREMENTS = 100;

  private final MarketData marketData;

  public QuoteEngine(MarketData marketData) {
    this.marketData = marketData;
  }

  /**
   * Calculates a loan with the lowest interest from the market.
   * 
   * @param loanAmount
   *          amount in GBP to be borrowed.
   * @param loanLengthInMonths
   *          number of months for the loan to be repaid.
   * @return A quote for a loan with the lowest market interest rate.
   * @throws LoanAmountOutOfRangeException
   *           If the amount of the loan fall outside the range accepted.
   * @throws IncorrectLoanAmountException
   *           If the amount of the loan is not in the increments expected.
   * @throws NoLendersException
   *           If there are no lenders in the market.
   * @throws NotEnoughMarketFundsException
   *           If there are not enough funds in the market to satisfy the loan
   *           request.
   * @throws MarketDataException
   *           If there is any problem accessing the market data source.
   */
  public LoanQuote getQuote(int loanAmount, int loanLengthInMonths)
      throws LoanAmountOutOfRangeException, IncorrectLoanAmountException, NoLendersException,
      NotEnoughMarketFundsException, MarketDataException {
    if (loanAmount < MIN_LOAN_AMOUNT || loanAmount > MAX_LOAN_AMOUNT) {
      throw new LoanAmountOutOfRangeException(MIN_LOAN_AMOUNT, MAX_LOAN_AMOUNT);
    }

    if (loanAmount % LOAN_AMOUNT_INCREMENTS != 0) {
      throw new IncorrectLoanAmountException(LOAN_AMOUNT_INCREMENTS);
    }
    
    List<Lender> lenders = marketData.getLenders();
    if (lenders.size() == 0) {
      throw new NoLendersException();
    }

    return getLowestRateLoan(lenders, loanAmount, loanLengthInMonths);
  }

  /**
   * Finds the combination of lenders that minimizes the interest rate to cover the amount requested.
   * 
   * @return A quote with the market's lowest interest rate.
   */
  private LoanQuote getLowestRateLoan(List<Lender> lenders, int loanAmount, int loanLengthInMonths)
      throws NotEnoughMarketFundsException {
    List<Lender> sortedLenders = lenders.stream()
        .sorted((Lender l1, Lender l2) -> l1.getRate().compareTo(l2.getRate()))
        .collect(Collectors.toList());

    LoanQuote quote = new LoanQuote(loanAmount, loanLengthInMonths);
    for (Lender lender : sortedLenders) {
      quote.borrow(lender);
      if (quote.isSatisfied()) {
        break;
      }
    }

    if (!quote.isSatisfied()) {
      throw new NotEnoughMarketFundsException();
    }

    return quote;
  }

}
