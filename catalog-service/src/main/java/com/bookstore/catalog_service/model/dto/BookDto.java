package com.bookstore.catalog_service.model.dto;

import com.bookstore.catalog_service.model.dto.enums.Availability;
import com.bookstore.catalog_service.model.dto.enums.Format;
import java.util.Set;
import lombok.Data;

/**
 * Book data transfer object.
 *
 * @author Filipa Simões
 */
@Data
public class BookDto {
  private long id;
  private String title;
  private String originalTitle;
  private String isbn;
  private String releaseDate;
  private String editionDate;
  private String genre;
  private String edition;
  private boolean series;
  private PublisherDto publisher;
  private String synopsis;
  private double price;
  private double promotionalPrice;
  private String collection;
  private String category;
  private Availability availability;
  private Format format;
  private Set<LanguageDto> languages;
  private Set<AuthorDto> authors;
  private Set<BookTagDto> bookTags;
  private int stockAvailable;
  double weight;
}
