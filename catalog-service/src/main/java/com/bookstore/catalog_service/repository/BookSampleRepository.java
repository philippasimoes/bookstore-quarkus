package com.bookstore.catalog_service.repository;

import com.bookstore.catalog_service.model.entity.BookSample;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Book Sample repository class.
 *
 * @author Filipa Simões
 */
@ApplicationScoped
public class BookSampleRepository implements PanacheRepository<BookSample> {}
