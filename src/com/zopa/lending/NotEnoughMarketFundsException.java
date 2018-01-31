package com.zopa.lending;

/** 
 * Exception to be thrown when there are not enough funds in the market to cover the 
 * amount requested.
 */
public class NotEnoughMarketFundsException extends Exception {

  private static final long serialVersionUID = 5822777235814019355L;
  
  NotEnoughMarketFundsException() {
    super("Not enough funds in the market.");
  }

}
