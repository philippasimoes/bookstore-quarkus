package com.bookstore.catalog_service.model.mapper;

import com.bookstore.catalog_service.model.dto.BookSampleDto;
import com.bookstore.catalog_service.model.entity.BookSample;
import org.mapstruct.Mapper;

/**
 * Class to map BookSampleDto to BookSample and BookSample to BookSampleDto.
 *
 * @author Filipa Simões
 */
@Mapper(componentModel = "cdi")
public interface BookSampleMapper {

    BookSampleDto toDto(BookSample bookSample);

    BookSample toEntity(BookSampleDto bookSampleDto);
}
