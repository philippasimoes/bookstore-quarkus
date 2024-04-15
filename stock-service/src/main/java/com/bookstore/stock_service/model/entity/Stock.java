package com.bookstore.stock_service.model.entity;

import jakarta.persistence.*;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * Stock JPA entity.
 *
 * @author Filipa Simões
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "stock", schema = "stockservice")
public class Stock {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "book_id", unique = true)
  private int bookId;

  @Column(name = "available_units")
  private int availableUnits;

  @Column(name = "pending_units")
  private int pendingUnits;

  @Column(name = "creation_date")
  @CreationTimestamp
  private Timestamp creationDate;

  @Column(name = "update_date")
  @UpdateTimestamp
  private Timestamp updateDate;
}
