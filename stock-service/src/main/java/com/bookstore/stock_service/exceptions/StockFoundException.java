package com.bookstore.stock_service.exceptions;

/**
 * Custom exception to be used when the stock entry already exists in database.
 *
 * @author Filipa Simões
 */
public class StockFoundException extends RuntimeException {

  public StockFoundException() {
    super();
  }

  public StockFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
