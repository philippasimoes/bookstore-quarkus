package com.bookstore.catalog_service.repository;

import com.bookstore.catalog_service.model.entity.Author;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class AuthorRepository implements PanacheRepository<Author> {

  public Optional<Author> findByIsni(String isni) {
    return find("isni", isni).stream().findFirst();
  }

  public List<Author> findByName(String name) {
    return list("name like ?1", "%" + name + "%");
  }

  public List<Author> getAllAuthors() {
    return findAll().stream().toList();
  }
}
