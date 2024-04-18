package com.bookstore.catalog_service.model.mapper;

import com.bookstore.catalog_service.model.dto.BookTagDto;
import com.bookstore.catalog_service.model.entity.BookTag;
import java.util.Set;
import org.mapstruct.Mapper;

/**
 * Class to map BookTagDto to BookTag and BookTag to BookTagDto.
 *
 * @author Filipa Simões
 */
@Mapper(componentModel = "cdi")
public interface BookTagMapper {
  BookTagDto toDto(BookTag bookTag);

  BookTag toEntity(BookTagDto bookTagDto);

  Set<BookTagDto> toDtoSet(Set<BookTag> bookTagSet);

  Set<BookTag> toEntitySet(Set<BookTagDto> bookTagDtoSet);
}
