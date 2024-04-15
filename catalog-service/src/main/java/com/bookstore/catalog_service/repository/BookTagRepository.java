package com.bookstore.catalog_service.repository;

import com.bookstore.catalog_service.model.entity.BookTag;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Optional;

/**
 * Book Tag repository class.
 *
 * @author Filipa Simões
 */
@ApplicationScoped
public class BookTagRepository implements PanacheRepository<BookTag> {

  public Optional<BookTag> findByValue(String value) {
    return find("value", value).stream().findFirst();
  }
}
