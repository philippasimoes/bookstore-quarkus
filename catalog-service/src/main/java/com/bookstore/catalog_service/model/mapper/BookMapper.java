package com.bookstore.catalog_service.model.mapper;

import com.bookstore.catalog_service.model.dto.BookDto;
import com.bookstore.catalog_service.model.entity.Book;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Class to map BookDto to Book and Book to BookDto.
 *
 * @author Filipa Simões
 */
@Mapper(componentModel = "cdi")
public interface BookMapper {

  BookDto toDto(Book book);

  @Mapping(target="authors", ignore = true)
  @Mapping(target="bookTags", ignore = true)
  @Mapping(target="languages", ignore = true)
  Book toEntity(BookDto bookDto);

  List<BookDto> toDtoList(List<Book> bookList);
}
