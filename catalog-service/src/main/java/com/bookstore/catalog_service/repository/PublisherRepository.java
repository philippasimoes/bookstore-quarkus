package com.bookstore.catalog_service.repository;

import com.bookstore.catalog_service.model.entity.Publisher;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class PublisherRepository implements PanacheRepository<Publisher> {

  public Publisher findByName(String name) {
    return find("name", name).firstResult();
  }
}
