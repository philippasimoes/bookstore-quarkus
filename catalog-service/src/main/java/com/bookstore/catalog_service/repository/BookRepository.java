package com.bookstore.catalog_service.repository;

import com.bookstore.catalog_service.model.dto.enums.Availability;
import com.bookstore.catalog_service.model.entity.Author;
import com.bookstore.catalog_service.model.entity.Book;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;

/**
 * Book repository class.
 *
 * @author Filipa Simões
 */
@ApplicationScoped
public class BookRepository implements PanacheRepository<Book> {

  public List<Book> getAllBooks() {
    return findAll().stream().toList();
  }

  public Optional<Book> findByIsbn(String isbn) {
    return find("isbn", isbn).stream().findFirst();
  }

  public List<Book> findByPriceBetween(double startPrice, double endPrice) {
    return find(
            "price between :startPrice and :endPrice",
            Parameters.with("startPrice", startPrice).and("endPrice", endPrice))
        .stream()
        .toList();
  }

  public List<Book> findBookBySeries(boolean series) {
    return find("series", series).stream().toList();
  }

  public List<Book> findByAvailability(Availability availability) {
    return find("availability", availability).stream().toList();
  }

  public List<Book> findByAuthor(long authorId) {
    var nativeQuery =
        "SELECT book FROM Book book JOIN book.authors author where author.id = :author_id";
    return find(nativeQuery, Parameters.with("author_id", authorId)).stream().toList();
  }

  public List<Book> findByLanguage(long languageId) {
    var nativeQuery =
        "SELECT book FROM Book book JOIN book.languages language where language.id = :language_id";
    return find(nativeQuery, Parameters.with("language_id", languageId)).stream().toList();
  }

  public List<Book> findByTag(long tagId) {
    var nativeQuery = "SELECT book FROM Book book JOIN book.bookTags tag where tag.id = :tag_id";
    return find(nativeQuery, Parameters.with("tag_id", tagId)).stream().toList();
  }

  public List<Book> findBooksByPublisher(long publisherId) {
    var nativeQuery =
        "SELECT book FROM Book book JOIN book.publisher publisher where publisher.id = :publisher_id";
    return find(nativeQuery, Parameters.with("publisher_id", publisherId)).stream().toList();
  }

  public void updateBookAvailability(long id, Availability availability) {
    update(
        "availability = :availability where id = :id",
        Parameters.with("availability", availability).and("id", id));
  }
}
