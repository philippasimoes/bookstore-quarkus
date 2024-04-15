package com.bookstore.catalog_service.service;

import com.bookstore.catalog_service.model.dto.BookDto;
import com.bookstore.catalog_service.model.dto.enums.Availability;
import com.bookstore.catalog_service.model.entity.Book;
import com.bookstore.catalog_service.model.entity.BookTag;
import com.bookstore.catalog_service.model.entity.Language;
import com.bookstore.catalog_service.model.entity.Publisher;
import com.bookstore.catalog_service.model.mapper.BookMapper;
import com.bookstore.catalog_service.repository.BookRepository;
import com.bookstore.catalog_service.repository.BookTagRepository;
import com.bookstore.catalog_service.repository.LanguageRepository;
import com.bookstore.catalog_service.repository.PublisherRepository;
import io.quarkus.hibernate.orm.panache.Panache;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class BookService {

  @Inject BookRepository bookRepository;
  @Inject BookTagRepository bookTagRepository;
  @Inject LanguageRepository languageRepository;
  @Inject PublisherRepository publisherRepository;
  @Inject BookMapper bookMapper;

  public List<BookDto> getAllBooks() {
    return bookMapper.toDtoList(bookRepository.getAllBooks());
  }

  public BookDto getBookById(long id) {
    return bookMapper.toDto(bookRepository.findById(id));
  }

  public List<BookDto> getBooksInPriceRange(double startPrice, double endPrice) {

    if (endPrice > startPrice) {
      return bookMapper.toDtoList(bookRepository.findByPriceBetween(startPrice, endPrice));
    } else throw new RuntimeException("The start price should bw lower than the end price");
  }

  public List<BookDto> getBooksBySeries(boolean isInASeries) {

    return bookMapper.toDtoList(bookRepository.findBookBySeries(isInASeries));
  }

  public List<BookDto> getBooksByAvailability(Availability availability) {
    return bookMapper.toDtoList(bookRepository.findByAvailability(availability));
  }

  public List<BookDto> getBooksByAuthor(long authorId) {

    return bookMapper.toDtoList(bookRepository.findByAuthor(authorId));
  }

  public List<BookDto> getBooksByLanguage(String languageCode) {

    Optional<Language> language = languageRepository.findByCode(languageCode);

    if (language.isPresent())
      return bookMapper.toDtoList(bookRepository.findByLanguage(language.get().getId()));
    else throw new NotFoundException("Language not found");
  }

  public List<BookDto> getBooksByTag(String tagValue) {

    Optional<BookTag> tag = bookTagRepository.findByValue(tagValue);

    if (tag.isPresent()) return bookMapper.toDtoList(bookRepository.findByTag(tag.get().getId()));
    else throw new NotFoundException("Tag not found");
  }

  public List<BookDto> getBooksByPublisher(long publisherId) {
    Publisher publisher = publisherRepository.findById(publisherId);

    if (publisher != null)
      return bookMapper.toDtoList(bookRepository.findBooksByPublisher(publisher.getId()));
    else throw new NotFoundException("Publisher not found");
  }

  @Transactional
  public BookDto addNewBook(BookDto bookDto) {

    Optional<Book> book2 = bookRepository.findByIsbn(bookDto.getIsbn());

    if (book2.isPresent()) {
      throw new RuntimeException("Book with ISBN " + bookDto.getIsbn() + "already exists.");
    } else {
      bookRepository.persist(bookMapper.toEntity(bookDto));
    }
    return bookMapper.toDto(bookRepository.findByIsbn(bookDto.getIsbn()).get());
  }

  @Transactional
  public String updateAvailability(Long id, Availability availability) {

    Book book = bookRepository.findById(id);

    if (id != null && availability != null && book != null) {
      bookRepository.updateBookAvailability(id, availability);
      return "The availability of the book with id " + id + " has been updated.";
    } else throw new NotFoundException("Book not found");
  }

  @Transactional
  public BookDto updateBook(BookDto bookDto) {
    Book book = bookRepository.findById(bookDto.getId());

    if (book != null) {
      Panache.getEntityManager().merge(bookMapper.toEntity(bookDto));
      return bookMapper.toDto(bookRepository.findById(bookDto.getId()));
    } else {
      throw new NotFoundException("Book not found in database");
    }
  }
}
