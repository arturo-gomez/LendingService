package com.zopa.lending.market;

import java.util.List;

/**
 * Defines functionality needed to be supported by any market data source. This
 * abstraction layer will allow to easily add new sources of market data
 * (Database, API, etc) without changing business logic of this Service.
 */
public interface MarketData {

  /**
   * Returns all the lenders available in the market.
   * 
   * @throws MarketDataException
   *           if there is any problem accessing market data source.
   */
  List<Lender> getLenders() throws MarketDataException;

}
