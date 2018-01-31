package com.zopa.lending;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.zopa.lending.market.Lender;
import com.zopa.lending.market.MarketData;
import com.zopa.lending.market.MarketDataException;

@RunWith(MockitoJUnitRunner.class)
class QuoteEngineTest {
  
  @Test
  void testGetQuote_MinimumQuoteSingleLender() throws Exception {
    List<Lender> lenders = new ArrayList<>();
    lenders.add(new Lender("Arturo", 0.06, 1000));
    lenders.add(new Lender("Raquel", 0.01, 1000));
    lenders.add(new Lender("Tomas", 0.06, 1000));
    MarketData market = Mockito.mock(MarketData.class);
    Mockito.when(market.getLenders()).thenReturn(lenders);
    
    QuoteEngine quoteEngine = new QuoteEngine(market);
    LoanQuote quote = quoteEngine.getQuote(1000, 3);
    assertEquals(0.01, quote.getRate());
  }
  
  @Test
  void testGetQuote_MinimumQuoteMultipleLender() throws Exception {
    List<Lender> lenders = new ArrayList<>();
    lenders.add(new Lender("Arturo", 0.06, 1000));
    lenders.add(new Lender("Raquel", 0.01, 500));
    lenders.add(new Lender("Isabella", 0.02, 500));
    MarketData market = Mockito.mock(MarketData.class);
    Mockito.when(market.getLenders()).thenReturn(lenders);
    
    QuoteEngine quoteEngine = new QuoteEngine(market);
    LoanQuote quote = quoteEngine.getQuote(1000, 3);
    assertEquals(0.015, quote.getRate());
  }

  @Test
  void testGetQuote_MarketWithoutLenders() throws MarketDataException {
    List<Lender> lenders = new ArrayList<>();
    MarketData market = Mockito.mock(MarketData.class);
    Mockito.when(market.getLenders()).thenReturn(lenders);
    
    QuoteEngine quoteEngine = new QuoteEngine(market);
    assertThrows(
        NoLendersException.class, () -> quoteEngine.getQuote(1000, 10));
  }
  
  @Test
  void testGetQuote_MarketWithLendersButNoFunds() throws MarketDataException {
    List<Lender> lenders = new ArrayList<>();
    lenders.add(new Lender("Arturo", 0.06, 100));
    MarketData market = Mockito.mock(MarketData.class);
    Mockito.when(market.getLenders()).thenReturn(lenders);
    
    QuoteEngine quoteEngine = new QuoteEngine(market);
    assertThrows(
        NotEnoughMarketFundsException.class, () -> quoteEngine.getQuote(1000, 10));
  }
  
  @Test
  void testGetQuote_LoanAmountBelowMin() throws MarketDataException {
    List<Lender> lenders = new ArrayList<>();
    MarketData market = Mockito.mock(MarketData.class);
    Mockito.when(market.getLenders()).thenReturn(lenders);
    
    QuoteEngine quoteEngine = new QuoteEngine(market);
    assertThrows(
        LoanAmountOutOfRangeException.class, () -> quoteEngine.getQuote(15001, 10));
  }
  
  @Test
  void testGetQuote_LoanAmountAboveMax() throws MarketDataException {
    List<Lender> lenders = new ArrayList<>();
    MarketData market = Mockito.mock(MarketData.class);
    Mockito.when(market.getLenders()).thenReturn(lenders);
    
    QuoteEngine quoteEngine = new QuoteEngine(market);
    assertThrows(
        LoanAmountOutOfRangeException.class, () -> quoteEngine.getQuote(100, 10));
  }
  
  @Test
  void testGetQuote_IncorrectAmountIncrement() throws MarketDataException {
    List<Lender> lenders = new ArrayList<>();
    MarketData market = Mockito.mock(MarketData.class);
    Mockito.when(market.getLenders()).thenReturn(lenders);
    
    QuoteEngine quoteEngine = new QuoteEngine(market);
    assertThrows(
        IncorrectLoanAmountException.class, () -> quoteEngine.getQuote(1105, 10));
  }

}
