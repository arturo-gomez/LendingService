package com.zopa.lending;

/** Exception to be thrown when no lenders are found in the market. */
public class NoLendersException extends Exception {

  private static final long serialVersionUID = -2580141979636366757L;
  
  NoLendersException() {
    super("There are no lenders in the market.");
  }

}
