package com.bookstore.stock_service.service;

import com.bookstore.stock_service.model.Book;
import com.bookstore.stock_service.model.entity.Stock;
import com.bookstore.stock_service.model.enums.StockStatus;
import com.bookstore.stock_service.repository.StockRepository;
import com.bookstore.stock_service.web.controller.BookResourceClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.hibernate.orm.panache.Panache;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

@ApplicationScoped
public class StockService {

  private static final Logger LOGGER = Logger.getLogger(StockService.class);

  @Inject @RestClient BookResourceClient bookResourceClient;

  @Inject StockRepository stockRepository;
  @Inject ObjectMapper objectMapper;

  /**
   * Used by catalog-service: when the book is created, a stock entry is created with the new book
   * id and zero units available.
   *
   * @param bookId the book identifier.
   */
  @Transactional
  public void addStock(long bookId) {

    Stock newStockEntry = new Stock();
    newStockEntry.setAvailableUnits(0);
    newStockEntry.setPendingUnits(0);
    newStockEntry.setBookId(bookId);
    stockRepository.persist(newStockEntry);
    LOGGER.info(String.format("New stock entry added for book with id %s.", bookId));
  }

  /**
   * Update book stock. Possible scenarios: 1) Book units are added by an admin (bookstore received
   * units from the publisher) and 2) Books are added to an order, and we need to remove them from
   * the available stock and add them to the pending stock;
   *
   * @param bookId the book identifier.
   * @param units the units to add or remove from stock.
   * @return the {@link StockStatus}.
   */
  public StockStatus updateStock(int bookId, int units) {

    Optional<Stock> stock = stockRepository.findByBookId(bookId);

    try {
      if (bookExists(bookId) && stock.isPresent()) {

        // add books to stock
        if (units > 0) return addAvailableUnits(stock.get(), units);
        // remove books from stock
        else return removeAvailableUnitsAndUpdatePendingUnits(stock.get(), units);

      } else {
        LOGGER.error(
            "Book is not present in catalog. Please insert the book in Catalog Service before adding stock.");
        return StockStatus.BOOK_NOT_FOUND;
      }

    } catch (JsonProcessingException e) {
      LOGGER.error("Error building message", e);
      return StockStatus.MESSAGE_ERROR;
    }
  }

  /**
   * Add units to available units.
   *
   * @param stock the stock entry.
   * @param units the units to be added.
   * @return UPDATED {@link StockStatus}
   * @throws JsonProcessingException when there's an error with messaging service.
   */
  @Transactional
  public StockStatus addAvailableUnits(Stock stock, int units) throws JsonProcessingException {

    int updatedUnits = stock.getAvailableUnits() + units;
    stock.setAvailableUnits(updatedUnits);
    Panache.getEntityManager().merge(stock);

    LOGGER.info(
        String.format(
            "Stock updated - book with id %s have %s units",
            stock.getBookId(), stock.getAvailableUnits()));

    return StockStatus.UPDATED;
  }

  /**
   * Book units are added to an order, and we need to remove the units from available units (and
   * they are not showing for the customer) and add them to the pending units (because the sale is
   * not completed).
   *
   * @param stock the stock entry.
   * @param units the units to be removed from the available units and added to the pending units.
   * @return INSUFFICIENT_STOCK, UPDATED or SOLD_OUT {@link StockStatus} according to the number of
   *     available units.
   * @throws JsonProcessingException when there's an error with messaging service.
   */
  @Transactional
  public StockStatus removeAvailableUnitsAndUpdatePendingUnits(Stock stock, int units)
      throws JsonProcessingException {

    if (units > stock.getAvailableUnits()) {
      return StockStatus.INSUFFICIENT_STOCK;
    } else {
      int updatedUnits =
          stock.getAvailableUnits() + units; // because the units is a negative number in this case
      stock.setAvailableUnits(updatedUnits);
      stock.setPendingUnits(stock.getPendingUnits() + units);
      Panache.getEntityManager().merge(stock);

      Stock updatedStock = stockRepository.findById(stock.getId());

      LOGGER.info(
          String.format(
              "Stock updated - book with id %s have %s available units. Pending units: %s",
              updatedStock.getBookId(),
              updatedStock.getAvailableUnits(),
              updatedStock.getPendingUnits()));

      // sending a message to catalog service (the queue is chosen according to the number of
      // available units)
      if (updatedStock.getAvailableUnits() > 0) {
        // producer.sendMessage(eventUpdatedQueue, buildMessage(stock.getBookId()));
        return StockStatus.UPDATED;
      } else {
        // producer.sendMessage(eventSoldOutQueue, buildMessage(stock.getBookId()));
        //        LOGGER.info(
        //            String.format("Stock updated - book with id %s is sold out",
        // stock.getBookId()));

        return StockStatus.SOLD_OUT;
      }
    }
  }

  /**
   * Removing the number of units from pending units and add them to the available units - used when
   * order is cancelled or items are removed from the order.
   *
   * @param bookId the book identifier.
   * @param units the number of units to remove from pending units and added to the available units.
   * @return UPDATED {@link StockStatus}
   */
  @Transactional
  public StockStatus removePendingUnitsAndUpdateAvailableUnits(int bookId, int units) {
    // removing units from pending units
    removePendingUnits(bookId, units);

    // adding units to available units again
    try {
      addAvailableUnits(stockRepository.findByBookId(bookId).get(), units);
    } catch (JsonProcessingException e) {
      LOGGER.error("Error building message", e);
      return StockStatus.MESSAGE_ERROR;
    }
    return StockStatus.UPDATED;
  }

  /**
   * Order is shipped and the books are not in the physical stock - removing the units from pending
   * units.
   *
   * @param bookId the book identifier.
   * @param units the book units to be removed from pending units.
   */
  @Transactional
  public StockStatus removePendingUnits(int bookId, int units) {
    Optional<Stock> stock = stockRepository.findByBookId(bookId);
    if (stock.isPresent()) {
      stock.get().setPendingUnits(stock.get().getPendingUnits() - units);
      Panache.getEntityManager().merge(stock.get());

      return StockStatus.UPDATED;
    } else throw new NotFoundException("Stock entry not found");
  }

  /**
   * Get the book available units.
   *
   * @param bookId the book identifier.
   * @return the number of available units.
   */
  public int getAvailableUnitsByBookId(int bookId) {

    if (stockRepository.findByBookId(bookId).isPresent()) {
      return stockRepository.findByBookId(bookId).get().getAvailableUnits();
    } else throw new NotFoundException("Stock entry not found");
  }

  /**
   * Build a message to be sent by rabbitmq to catalog service with the book available units.
   *
   * @param bookId the book identifier.
   * @return the message containing the book identifier and the available units.
   * @throws JsonProcessingException when there's an error with messaging service.
   */
  private String buildMessage(int bookId) throws JsonProcessingException {

    Map<Integer, Integer> map = new HashMap<>();
    map.put(bookId, getAvailableUnitsByBookId(bookId));

    return objectMapper.writeValueAsString(map);
  }

  /**
   * Validate if a book is registered in the catalog service.
   *
   * @param bookId the book identifier.
   * @return true if the book exists.
   * @throws JsonProcessingException when there's an error reading the jwt token.
   */
  private boolean bookExists(int bookId) throws JsonProcessingException {

    try {
      Book book = bookResourceClient.getBookById(bookId);

      if (book != null) {
        LOGGER.infof(
            "Connection with catalog service successful: book with title %s will have stock updated.",
            book.title());
        return true;
      } else {
        LOGGER.info(
            "Connection with catalog service successful: book with title does not exist in catalog.");
        return false;
      }
    } catch (Exception e) {
      LOGGER.error("Error connecting to catalog service.");
      return false;
    }
  }
}
