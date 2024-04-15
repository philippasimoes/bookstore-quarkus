package com.bookstore.catalog_service.service;

import com.bookstore.catalog_service.model.dto.AuthorDto;
import com.bookstore.catalog_service.model.entity.Author;
import com.bookstore.catalog_service.model.mapper.AuthorMapper;
import com.bookstore.catalog_service.repository.AuthorRepository;
import io.quarkus.hibernate.orm.panache.Panache;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class AuthorService {

  @Inject AuthorRepository authorRepository;
  @Inject AuthorMapper authorMapper;

  public List<AuthorDto> getAllAuthors() {
    return authorMapper.toDtoList(authorRepository.getAllAuthors());
  }

  public AuthorDto findByIsni(String isni) {
    return authorMapper.toDto(
        authorRepository
            .findByIsni(isni)
            .orElseThrow(() -> new NotFoundException("Author not found")));
  }

  public List<AuthorDto> findByName(String name) {
    return authorMapper.toDtoList(authorRepository.findByName(name));
  }

  @Transactional
  public AuthorDto addNewAuthor(AuthorDto authorDto) {

    authorRepository.persist(authorMapper.toEntity(authorDto));

    return authorMapper.toDto(
        authorRepository
            .findByIsni(authorDto.getIsni())
            .orElseThrow(() -> new NotFoundException("Author not found")));
  }

  @Transactional
  public AuthorDto updateAuthor(AuthorDto authorDto) {

    Optional<Author> author = authorRepository.findByIdOptional(authorDto.getId());

    if (author.isPresent()) {
      Panache.getEntityManager().merge(authorMapper.toEntity(authorDto));
    } else throw new NotFoundException("Author not found");

    return authorMapper.toDto(
        authorRepository
            .findByIdOptional(authorDto.getId())
            .orElseThrow(() -> new NotFoundException("Author not found")));
  }
}
