package com.zopa.lending.market;

/** Lender abstraction. */
public class Lender {

  private final String name;
  private final Double rate;
  private final int amount;

  public Lender(String name, double rate, int amount) {
    this.name = name;
    this.rate = rate;
    this.amount = amount;
  }

  /** Returns the lender name. */
  public String getName() {
    return name;
  }

  /** Returns the interest rate from this lender. */
  public Double getRate() {
    return rate;
  }

  /** Returns the amount available from this lender. */
  public int getAmount() {
    return amount;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj.getClass() != Lender.class) {
      return false;
    }

    Lender lender = (Lender) obj;

    return name.equals(lender.getName()) 
        && rate.equals(lender.getRate())
        && (amount == lender.getAmount());
  }

}
