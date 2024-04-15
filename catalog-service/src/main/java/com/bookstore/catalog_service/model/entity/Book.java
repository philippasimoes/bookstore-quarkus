package com.bookstore.catalog_service.model.entity;

import com.bookstore.catalog_service.model.dto.enums.Availability;
import com.bookstore.catalog_service.model.dto.enums.Format;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Book JPA entity.
 *
 * @author Filipa Simões
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
    name = "book",
    schema = "catalogservice",
    indexes = {@Index(name = "idx_book_id_creation_date", columnList = "id, creation_date")})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Book extends BaseEntity {

  @Column private String title;

  @Column(name = "original_title")
  private String originalTitle;

  @Column(unique = true)
  private String isbn;

  @Column(name = "release_date")
  private String releaseDate;

  @Column(name = "edition_date")
  private String editionDate;

  @Column private String genre;

  @Column private String edition;

  @Column private boolean series;

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "publisher_id")
  private Publisher publisher;

  @Column private String synopsis;

  @Column private double price;

  @Column(name = "promotional_price")
  private double promotionalPrice;

  @Column private String collection;

  @Column private String category;

  @Enumerated(EnumType.STRING)
  private Availability availability;

  @Enumerated(EnumType.STRING)
  private Format format;

  @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinTable(
      name = "book_language",
      joinColumns = @JoinColumn(name = "book_id"),
      inverseJoinColumns = @JoinColumn(name = "language_id"),
      schema = "catalogservice")
  private Set<Language> languages;

  @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinTable(
      name = "book_author",
      joinColumns = @JoinColumn(name = "book_id"),
      inverseJoinColumns = @JoinColumn(name = "author_id"),
      schema = "catalogservice")
  private Set<Author> authors;

  @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinTable(
      name = "book_tag",
      joinColumns = @JoinColumn(name = "book_id"),
      inverseJoinColumns = @JoinColumn(name = "tag_id"),
      schema = "catalogservice")
  private Set<BookTag> bookTags;

  @Column(name = "stock_available", nullable = false)
  private int stockAvailable;

  @Column double weight;
}
