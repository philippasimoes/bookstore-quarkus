package com.bookstore.stock_service.web.controller;

import com.bookstore.stock_service.model.Book;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.reactive.RestPath;

@Path("/api/books")
@RegisterRestClient(baseUri = "stork://catalog-service")
public interface BookResourceClient {

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    Book getBookById(@RestPath("id") long id);
}
