package com.zopa.lending.market;

import static org.junit.jupiter.api.Assertions.*;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import org.junit.jupiter.api.Test;

class CsvMarketDataTest {

  private static final String CSV_DATA = 
      "Lender,Rate,Available\n" 
      + "Arturo,0.06,1000\n"
      + "Raquel,0.065,1500\n";
  
  private static final String CSV_DATA_CORRUPTED = 
      "Lender,Rate,Available\n" 
      + "Arturo,abc,1000\n"
      + "Raquel,0.065,1500\n";
  
  @Test
  void testGetLenders_AllLendersShouldBeIncluded() {
    Reader reader = new StringReader(CSV_DATA);
    MarketData marketData = new CsvMarketData(reader);
    List<Lender> lenders = null;
    try {
      lenders = marketData.getLenders();
    } catch (MarketDataException e) {
      fail("Exception not expected.");
    }
    Lender[] expected = new Lender[] {
        new Lender("Arturo", 0.06, 1000),
        new Lender("Raquel", 0.065, 1500)
    };
    assertArrayEquals(expected, lenders.toArray());
  }
  
  @Test
  void testGetLenders_ResilientToCorruptedData() {
    Reader reader = new StringReader(CSV_DATA_CORRUPTED);
    MarketData marketData = new CsvMarketData(reader);
    List<Lender> lenders = null;
    try {
      lenders = marketData.getLenders();
    } catch (MarketDataException e) {
      fail("Exception not expected.");
    }
    Lender raquel = new Lender("Raquel", 0.065, 1500);
    assertTrue(lenders.contains(raquel));
  }

}
