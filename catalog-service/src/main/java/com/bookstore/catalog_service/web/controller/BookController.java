package com.bookstore.catalog_service.web.controller;

import com.bookstore.catalog_service.model.dto.BookDto;
import com.bookstore.catalog_service.model.dto.enums.Availability;
import com.bookstore.catalog_service.service.BookService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.ResponseStatus;
import org.jboss.resteasy.reactive.RestPath;

import java.util.List;

@Path("/api/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookController {

  @Inject BookService bookService;

  @GET
  public List<BookDto> getAllBooks() {
    return bookService.getAllBooks();
  }

  @GET
  @Path("/{id}")
  public BookDto getBookById(@RestPath("id") long id) {
    return bookService.getBookById(id);
  }

  @GET
  @Path("/price")
  public List<BookDto> getBooksInPriceRange(
      @QueryParam("startPrice") double startPrice, @QueryParam("endPrice") double endPrice) {
    return bookService.getBooksInPriceRange(startPrice, endPrice);
  }

  @GET
  @Path("/series")
  public List<BookDto> getBooksBySeries(@QueryParam("series") boolean series) {
    return bookService.getBooksBySeries(series);
  }

  @GET
  @Path("/availability")
  public List<BookDto> getAvailableBooks(@QueryParam("availability") Availability availability) {
    return bookService.getBooksByAvailability(availability);
  }

  @GET
  @Path("/author/{id}")
  public List<BookDto> getBooksByAuthor(@RestPath("id") long authorId) {
    return bookService.getBooksByAuthor(authorId);
  }

  @GET
  @Path("/language")
  public List<BookDto> getBooksByLanguage(@QueryParam("code") String languageCode) {
    return bookService.getBooksByLanguage(languageCode);
  }

  @GET
  @Path("/tag")
  public List<BookDto> getBooksByTag(@QueryParam("value") String tagValue) {
    return bookService.getBooksByTag(tagValue);
  }

  @GET
  @Path("/publisher")
  public List<BookDto> getBooksByPublisher(@QueryParam("id") long publisherId) {
    return bookService.getBooksByPublisher(publisherId);
  }

  @POST
  @ResponseStatus(201)
  public BookDto addBook(BookDto bookDto) {
    return bookService.addNewBook(bookDto);
  }

  @PUT
  @ResponseStatus(200)
  public BookDto updateBook(BookDto bookDto) {
    return bookService.updateBook(bookDto);
  }

  @PATCH
  @Path("/{id}")
  @ResponseStatus(200)
  public String updateAvailability(
      @RestPath("id") long id, @QueryParam("availability") Availability availability) {
    return bookService.updateAvailability(id, availability);
  }
}
