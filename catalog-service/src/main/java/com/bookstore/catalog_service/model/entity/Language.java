package com.bookstore.catalog_service.model.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Language JPA entity.
 *
 * @author Filipa Simões
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "language", schema = "catalogservice")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Language extends BaseEntity {

  @Column(unique = true)
  private String code;

  @ManyToMany(fetch = FetchType.LAZY, mappedBy = "languages")
  private Set<Book> books;
}
