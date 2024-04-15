package com.bookstore.catalog_service.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Book Sample JPA entity.
 *
 * @author Filipa Simões
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "book_sample", schema = "catalogservice")
public class BookSample extends BaseEntity {


  @OneToOne
  @JoinColumn(name = "book_id")
  private Book book;

  @Column private String sample;
}
