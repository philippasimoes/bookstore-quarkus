package com.bookstore.stock_service.repository;

import com.bookstore.stock_service.model.entity.Stock;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import java.util.Optional;

public class StockRepository implements PanacheRepository<Stock> {

    public Optional<Stock> findByBookId(long bookId){
        return find("book_id", bookId).stream().findFirst();
    }
}
