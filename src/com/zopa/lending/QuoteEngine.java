package com.zopa.lending;

import java.util.List;
import java.util.stream.Collectors;

import com.zopa.lending.market.Lender;
import com.zopa.lending.market.MarketData;

public class QuoteEngine {

  private static final int MIN_LOAN_AMOUNT = 1000;
  private static final int MAX_LOAN_AMOUNT = 15000;
  private static final int LOAN_AMOUNT_INCREMENTS = 100;

  private final MarketData marketData;

  public QuoteEngine(MarketData marketData) {
    this.marketData = marketData;
  }

  public LoanQuote getQuote(int loanAmount, int loanLengthInMonths) 
      throws LoanAmountOutOfRangeException, IncorrectLoanAmountException, NoLendersException, 
      NotEnoughMarketFundsException {
    if (loanAmount < MIN_LOAN_AMOUNT || loanAmount > MAX_LOAN_AMOUNT) {
      throw new LoanAmountOutOfRangeException(MIN_LOAN_AMOUNT, MAX_LOAN_AMOUNT);
    }

    if (loanAmount % LOAN_AMOUNT_INCREMENTS != 0) {
      throw new IncorrectLoanAmountException(LOAN_AMOUNT_INCREMENTS);
    }

    List<Lender> sortedLenders = marketData.getLenders().stream()
        .sorted((Lender l1, Lender l2) -> l1.getRate().compareTo(l2.getRate()))
        .collect(Collectors.toList());
    return getLowestRateLoan(sortedLenders, loanAmount, loanLengthInMonths);
  }

  private LoanQuote getLowestRateLoan(List<Lender> lenders, int loanAmount, int loanLengthInMonths) 
      throws NoLendersException, NotEnoughMarketFundsException {
    if (lenders.size() == 0) {
      throw new NoLendersException();
    }

    LoanQuote quote = new LoanQuote(loanAmount, loanLengthInMonths);
    for (Lender lender : lenders) {
      quote.borrowFrom(lender);
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
