package com.bookstore.stock_service.web.controller;

import com.bookstore.stock_service.model.enums.StockStatus;
import com.bookstore.stock_service.service.StockService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import org.jboss.resteasy.reactive.ResponseStatus;
import org.jboss.resteasy.reactive.RestPath;
import org.jboss.resteasy.reactive.RestResponse;

@Path("/api/stock")
public class StockController {

  @Inject StockService stockService;

  @POST
  @ResponseStatus(201)
  @Path("/book/{book_id}")
  public String createStock(@RestPath("book_id") long bookId) {
    stockService.addStock(bookId);
    return "Stock created";
  }

  @PUT
  @ResponseStatus(200)
  @Path("/book/{book_id}")
  public RestResponse<String> updateStock(
      @RestPath("book_id") int bookId, @QueryParam("units") int units)
      throws JsonProcessingException {
    StockStatus stockStatus = stockService.updateStock(bookId, units);
    return validateStockStatus(stockStatus);
  }

  @PATCH
  @Path("/book/{book_id}")
  public RestResponse<String> removePendingUnits(
      @RestPath("book_id") int bookId, @QueryParam("pending-units") int pendingUnits) {

    return validateStockStatus(stockService.removePendingUnits(bookId, pendingUnits));
  }

  @PATCH
  @ResponseStatus(200)
  @Path("/book/order-cancelled/{book_id}")
  public RestResponse<String> removePendingUnitsAndUpdateAvailableUnits(
      @RestPath("book_id") int bookId, @QueryParam("units") int units) {
    return validateStockStatus(
        stockService.removePendingUnitsAndUpdateAvailableUnits(bookId, units));
  }

  @GET
  @Path("/{bookId}")
  public Integer stockIsAboveZero(@RestPath("bookId") int bookId) {

    return stockService.getAvailableUnitsByBookId(bookId);
  }

  /**
   * Validate stock status enum and return an adequate response entity.
   *
   * @param stockStatus the stock status {@link StockStatus}
   * @return a response entity.
   */
  private RestResponse<String> validateStockStatus(StockStatus stockStatus) {

    switch (stockStatus) {
      case UPDATED -> {
        return RestResponse.ResponseBuilder.ok("Stock updated").build();
      }
      case SOLD_OUT -> {
        return RestResponse.ResponseBuilder.ok("Book sold out").build();
      }
      case INSUFFICIENT_STOCK -> {
        return RestResponse.status(412);
      }
      case BOOK_NOT_FOUND -> {
        return RestResponse.notFound();
      }
      case MESSAGE_ERROR -> {
        return RestResponse.serverError();
      }
      default -> {
        return RestResponse.notModified();
      }
    }
  }
}
