package com.bookstore.catalog_service.repository;

import com.bookstore.catalog_service.model.entity.Language;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

/**
 * Language repository class.
 *
 * @author Filipa Simões
 */
@ApplicationScoped
public class LanguageRepository implements PanacheRepository<Language> {

  public Optional<Language> findByCode(String code) {
    return find("code", code).stream().findFirst();
  }
}
