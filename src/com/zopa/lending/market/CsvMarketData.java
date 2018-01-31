package com.zopa.lending.market;

import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/** Abstraction of lender data stored in a CSV file. */
public class CsvMarketData implements MarketData {
  
  private static final String AVAILABLE_COLUMN = "Available";
  private static final String RATE_COLUMN = "Rate";
  private static final String LENDER_COLUMN = "Lender";
  private final Reader marketDataReader;

	public CsvMarketData(Reader marketDataReader) {
	  this.marketDataReader = marketDataReader;
	}

	@Override
	public List<Lender> getLenders() throws MarketDataException {
	  List<CSVRecord> records = null;
	  try {
		  records = loadRecords();
	  } catch (IOException e) {
	    throw new MarketDataException(e);
	  }
	  
	  return parseLenders(records);
	}
	
	/** Returns the records from the CSV file. */
	private List<CSVRecord> loadRecords() throws IOException {
	  CSVParser parser = CSVParser.parse(marketDataReader, CSVFormat.DEFAULT.withFirstRecordAsHeader());
    return parser.getRecords(); 
	}
	
	/** Transforms the list of {@link CSVRecord} provided into a list of {@link Lender}. */
	private List<Lender> parseLenders(List<CSVRecord> records) {
	  return records.stream().map(record -> {
	    String lender = record.get(LENDER_COLUMN);
	    try {
	      return new Lender(
	          lender, 
	          Double.parseDouble(record.get(RATE_COLUMN)), 
	          Integer.parseInt(record.get(AVAILABLE_COLUMN)));
	    } catch (NumberFormatException e) {
	      return new Lender(lender, 0.0, 0);
	    } 
	  }).collect(Collectors.toList());
	}
	
	

}
