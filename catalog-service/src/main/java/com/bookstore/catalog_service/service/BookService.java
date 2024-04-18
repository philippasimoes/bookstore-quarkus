package com.bookstore.catalog_service.service;

import com.bookstore.catalog_service.model.dto.AuthorDto;
import com.bookstore.catalog_service.model.dto.BookDto;
import com.bookstore.catalog_service.model.dto.BookTagDto;
import com.bookstore.catalog_service.model.dto.LanguageDto;
import com.bookstore.catalog_service.model.dto.enums.Availability;
import com.bookstore.catalog_service.model.entity.Author;
import com.bookstore.catalog_service.model.entity.Book;
import com.bookstore.catalog_service.model.entity.BookTag;
import com.bookstore.catalog_service.model.entity.Language;
import com.bookstore.catalog_service.model.entity.Publisher;
import com.bookstore.catalog_service.model.mapper.AuthorMapper;
import com.bookstore.catalog_service.model.mapper.BookMapper;
import com.bookstore.catalog_service.model.mapper.BookTagMapper;
import com.bookstore.catalog_service.model.mapper.LanguageMapper;
import com.bookstore.catalog_service.repository.AuthorRepository;
import com.bookstore.catalog_service.repository.BookRepository;
import com.bookstore.catalog_service.repository.BookTagRepository;
import com.bookstore.catalog_service.repository.LanguageRepository;
import com.bookstore.catalog_service.repository.PublisherRepository;
import com.bookstore.catalog_service.web.controller.StockResourceClient;
import io.quarkus.hibernate.orm.panache.Panache;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import org.jobrunr.scheduling.JobBuilder;
import org.jobrunr.scheduling.JobScheduler;

@ApplicationScoped
public class BookService {

  private static final Logger LOGGER = Logger.getLogger(BookService.class);

  @Inject BookRepository bookRepository;
  @Inject BookTagRepository bookTagRepository;
  @Inject LanguageRepository languageRepository;
  @Inject PublisherRepository publisherRepository;
  @Inject AuthorRepository authorRepository;
  @Inject BookMapper bookMapper;
  @Inject AuthorMapper authorMapper;
  @Inject LanguageMapper languageMapper;
  @Inject BookTagMapper bookTagMapper;
  @Inject JobScheduler jobScheduler;

  @Inject @RestClient StockResourceClient stockResourceClient;

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

  public BookDto addNewBook(BookDto bookDto) {

    Optional<Book> book = bookRepository.findByIsbn(bookDto.getIsbn());
    if (book.isPresent()) {
      throw new RuntimeException("Book with ISBN " + bookDto.getIsbn() + "already exists.");
    } else {

      Optional<Book> savedBook = addBookToDatabase(bookDto);

      if (savedBook.isPresent()) {
        addStockEntry(savedBook.get().getId());
      } else {
        System.out.println("error");
      }
      return bookMapper.toDto(savedBook.get());
    }
  }

  @Transactional
  public Optional<Book> addBookToDatabase(BookDto bookDto) {

    Set<Language> languageSet = getLanguageFromBook(bookDto);
    Set<BookTag> bookTagSet = getTagsFromBook(bookDto);
    Set<Author> authorSet = getAuthorsFromBook(bookDto);

    Book newBook = bookMapper.toEntity(bookDto);
    newBook.setPublisher(getPublisherFromBook(bookDto));
    newBook.setLanguages(languageSet);
    newBook.setBookTags(bookTagSet);
    newBook.setAuthors(authorSet);

    bookRepository.persist(newBook);

    return bookRepository.findByIsbn(bookDto.getIsbn());
  }

  /**
   * Auxiliary method that get the authors from a book - if the author does not exist in database it
   * will be created.
   *
   * @param bookDto the book (data transfer object) sent in request.
   * @return a set of authors.
   */
  private Set<Author> getAuthorsFromBook(BookDto bookDto) {

    Set<Author> authorSet = new HashSet<>();

    for (AuthorDto authorDto : bookDto.getAuthors()) {
      Optional<Author> author = authorRepository.findByIsni(authorDto.getIsni());

      if (author.isPresent()) {
        authorSet.add(author.get());
      } else {
        authorRepository.persist(authorMapper.toEntity(authorDto));

        authorSet.add(authorRepository.findByIsni(authorDto.getIsni()).get());
      }
    }

    return authorSet;
  }

  /**
   * Auxiliary method that get the tags from a book - if the tag does not exist in database it will
   * be created.
   *
   * @param bookDto the book (data transfer object) sent in request.
   * @return a set of tags.
   */
  private Set<BookTag> getTagsFromBook(BookDto bookDto) {

    Set<BookTag> bookTagSet = new HashSet<>();

    for (BookTagDto bookTagDto : bookDto.getBookTags()) {
      Optional<BookTag> bookTag = bookTagRepository.findByValue(bookTagDto.getValue());

      if (bookTag.isPresent()) {
        bookTagSet.add(bookTag.get());
      } else {
        bookTagRepository.persist(bookTagMapper.toEntity(bookTagDto));
        bookTagSet.add(bookTagRepository.findByValue(bookTagDto.getValue()).get());
      }
    }

    return bookTagSet;
  }

  /**
   * Auxiliary method that get the languages from a book - if the language does not exist in
   * database it will be created.
   *
   * @param bookDto the book (data transfer object) sent in request.
   * @return a set of languages.
   */
  private Set<Language> getLanguageFromBook(BookDto bookDto) {

    Set<Language> languageSet = new HashSet<>();

    for (LanguageDto languageDto : bookDto.getLanguages()) {
      Optional<Language> language = languageRepository.findByCode(languageDto.getCode());

      if (language.isPresent()) {
        languageSet.add(language.get());
      } else {
        languageRepository.persist(languageMapper.toDto(languageDto));
        languageSet.add(languageRepository.findByCode(languageDto.getCode()).get());
      }
    }
    return languageSet;
  }

  private Publisher getPublisherFromBook(BookDto bookDto) {
    Publisher publisher = publisherRepository.findByName(bookDto.getPublisher().getName());

    if (publisher == null) {
      Publisher newPublisher = new Publisher();
      newPublisher.setName(bookDto.getPublisher().getName());
      newPublisher.setEmail(bookDto.getPublisher().getEmail());
      newPublisher.setPhoneNumber(bookDto.getPublisher().getPhoneNumber());
      publisherRepository.persist(newPublisher);
    }
    return publisher;
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

  public void addStockEntry(long id) {
    try {
      addStockEntryFallback(id);
      LOGGER.info("Connected to stock service.");
    } catch (Exception e) {
      LOGGER.errorf("Error connecting with stock service. %s", e.getMessage());
      jobScheduler.create(
          JobBuilder.aJob()
              .scheduleIn(Duration.ofMinutes(1))
              .withAmountOfRetries(2)
              .withDetails(() -> addStockEntryFallback(id)));
    }
  }

  public void addStockEntryFallback(long id) {
    stockResourceClient.createStock(id);
  }
}
