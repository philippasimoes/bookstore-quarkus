package com.bookstore.stock_service.model.enums;

/**
 * The Stock Status class, used to indicate the status of the stock entity when the number of units
 * is updated.
 *
 * @author Filipa Simões
 */
public enum StockStatus {
    UPDATED,
    SOLD_OUT,
    INSUFFICIENT_STOCK,
    MESSAGE_ERROR,
    BOOK_NOT_FOUND
}
