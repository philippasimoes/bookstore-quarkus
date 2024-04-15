package com.bookstore.catalog_service.web.controller;

import com.bookstore.catalog_service.model.dto.AuthorDto;
import com.bookstore.catalog_service.service.AuthorService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/api/authors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthorController {

  @Inject AuthorService authorService;

  @GET
  public List<AuthorDto> getAllAuthors() {
    return authorService.getAllAuthors();
  }

  @GET
  @Path("/name")
  public List<AuthorDto> getAuthorByName(@QueryParam("value") String name) {
    return authorService.findByName(name);
  }

  @POST
  public AuthorDto addNewAuthor(AuthorDto authorDto) {
    return authorService.addNewAuthor(authorDto);
  }

  @PUT
  public AuthorDto updateAuthor(AuthorDto authorDto) {
    return authorService.updateAuthor(authorDto);
  }
}
