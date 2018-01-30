package com.zopa.lending.market;

public class Lender {
	
	private final String name;
	private final Double rate;
	private final int amount;
	
	public Lender(String name, double rate, int amount) {
		this.name = name;
		this.rate = rate;
		this.amount = amount;
	}

	public String getName() {
		return name;
	}

	public Double getRate() {
		return rate;
	}

	public int getAmount() {
		return amount;
	}

}
