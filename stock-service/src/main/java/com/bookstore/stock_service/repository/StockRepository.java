package com.bookstore.stock_service.repository;

import com.bookstore.stock_service.model.entity.Stock;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class StockRepository implements PanacheRepository<Stock> {

    public Optional<Stock> findByBookId(long bookId){
        return find("bookId", bookId).stream().findFirst();
    }
}
