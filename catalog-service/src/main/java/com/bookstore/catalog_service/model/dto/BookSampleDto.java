package com.bookstore.catalog_service.model.dto;

import lombok.Data;

/**
 * BookSample data transfer object.
 *
 * @author Filipa Simões
 */
@Data
public class BookSampleDto {

  private int id;
  private int bookId;
  private String sample;
}
