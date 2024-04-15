package com.bookstore.catalog_service.model.mapper;

import com.bookstore.catalog_service.model.dto.BookDto;
import com.bookstore.catalog_service.model.entity.Book;
import java.util.List;
import org.mapstruct.Mapper;

/**
 * Class to map BookDto to Book and Book to BookDto.
 *
 * @author Filipa Simões
 */
@Mapper(componentModel = "cdi")
public interface BookMapper {

  BookDto toDto(Book book);

  Book toEntity(BookDto bookDto);

  List<BookDto> toDtoList(List<Book> bookList);
}
