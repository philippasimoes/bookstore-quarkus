package com.bookstore.catalog_service.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "publisher", schema = "catalogservice")
public class Publisher extends BaseEntity {

  @Column(unique = true)
  private String name;

  @Column private String email;

  @Column(name = "phone_number")
  private String phoneNumber;
  
}
