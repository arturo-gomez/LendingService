package com.zopa.lending.market;

/** Exception to be thrown when there is any error accessing the data source. */
public class MarketDataException extends Exception {

  private static final long serialVersionUID = -8740986324298621662L;

  public MarketDataException(Exception source) {
    super(source);
  }

}
