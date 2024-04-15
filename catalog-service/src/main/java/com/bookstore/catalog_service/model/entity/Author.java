package com.bookstore.catalog_service.model.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Author JPA entity.
 *
 * @author Filipa Simões
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "author", schema = "catalogservice")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Author extends BaseEntity {

  @Column(unique = true)
  private String isni;

  @Column
  private String name;

  @Column(name = "original_full_name")
  private String originalFullName;

  @Column(name = "date_of_birth")
  private String dateOfBirth;

  @Column(name = "place_of_birth")
  private String placeOfBirth;

  @Column(name = "date_of_death")
  private String dateOfDeath;

  @Column(name = "place_of_death")
  private String placeOfDeath;

  @Column private String about;

  @ManyToMany(fetch = FetchType.LAZY, mappedBy = "authors")
  private Set<Book> books;
}
