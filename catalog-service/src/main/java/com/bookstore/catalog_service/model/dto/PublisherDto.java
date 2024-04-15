package com.bookstore.catalog_service.model.dto;

import lombok.Data;

/**
 * Publisher data transfer object.
 *
 * @author Filipa Simões
 */
@Data
public class PublisherDto {

  private int id;
  private String name;
  private String email;
  private String phoneNumber;
}
