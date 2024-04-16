package com.bookstore.catalog_service.web.controller;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.reactive.RestPath;

@Path("/stock")
@RegisterRestClient
public interface StockResourceClient {

  @POST
  @Path("/book/{book_id}")
  String createStock(@RestPath("book_id") long bookId);
}
