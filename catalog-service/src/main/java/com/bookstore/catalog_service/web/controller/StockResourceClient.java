package com.bookstore.catalog_service.web.controller;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.reactive.RestPath;

import java.util.concurrent.CompletionStage;

@Path("/stock")
@RegisterRestClient
public interface StockResourceClient {

  @POST
  @Path("/book/{book_id}")
  //CompletionStage<String> createStock(@RestPath("book_id") long bookId);
  String createStock(@RestPath("book_id") long bookId);
}
