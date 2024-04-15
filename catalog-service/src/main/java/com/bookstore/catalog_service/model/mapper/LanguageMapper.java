package com.bookstore.catalog_service.model.mapper;

import com.bookstore.catalog_service.model.dto.LanguageDto;
import com.bookstore.catalog_service.model.entity.Language;
import java.util.Set;
import org.mapstruct.Mapper;

/**
 * Class to map LanguageDto to Language and Language to LanguageDto.
 *
 * @author Filipa Simões
 */
@Mapper
public interface LanguageMapper {

  LanguageDto toEntity(Language language);

  Language toDto(LanguageDto languageDto);

  Set<LanguageDto> toDtoSet(Set<Language> languageSet);

  Set<Language> toEntitySet(Set<LanguageDto> languageDtoSet);
}
