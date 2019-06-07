package com.excella.reactor.model;

import com.excella.reactor.domain.Book;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = BookDto.BookDtoBuilder.class)
public class BookDto implements DomainMapper<Book> {
  @NonNull private String title;

  @NonNull private String author;

  private int yearPublished;

  @JsonPOJOBuilder(withPrefix = "")
  public static class BookDtoBuilder {}

  @Override
  public Book toDomain() {
    return Book.builder()
        .title(getTitle())
        .author(getAuthor())
        .yearPublished(getYearPublished())
        .build();
  }
}
