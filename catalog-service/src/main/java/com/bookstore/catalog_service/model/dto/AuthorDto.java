package com.bookstore.catalog_service.model.dto;

import lombok.Data;

@Data
public class AuthorDto {
  private long id;
  private String isni;
  private String name;
  private String originalFullName;
  private String dateOfBirth;
  private String placeOfBirth;
  private String dateOfDeath;
  private String placeOfDeath;
  private String about;
}
