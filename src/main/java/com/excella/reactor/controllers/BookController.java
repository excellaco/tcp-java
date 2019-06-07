package com.excella.reactor.controllers;

import com.excella.reactor.common.exceptions.BookNotFoundException;
import com.excella.reactor.common.reactor.MonoUtils;
import com.excella.reactor.domain.Book;
import com.excella.reactor.model.BookDto;
import com.excella.reactor.repositories.BookRepository;
import java.util.List;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/book")
@Slf4j
public class BookController {
  private final BookRepository bookRepository;

  @Autowired
  BookController(BookRepository bookRepository) {
    this.bookRepository = bookRepository;
  }

  @GetMapping(value = "/", name = "Get all books", produces = "application/json")
  Flux<Book> getAllBooks() {
    return MonoUtils.retrieveAsList(bookRepository::findAll)
        .doOnSubscribe(result -> log.info("Getting all books"));
  }

  @GetMapping(
      value = "/{bookId}",
      name = "Get book by id",
      produces = MediaType.APPLICATION_JSON_VALUE)
  Mono<Book> getBookById(@PathVariable Long bookId) {
    return MonoUtils.fromCallableOpt(() -> bookRepository.findById(bookId))
        .doOnSubscribe(result -> log.info("Getting book id {}", bookId))
        .switchIfEmpty(Mono.error(BookNotFoundException.of("No book was found")));
  }

  @PostMapping(value = "/", name = "Add a new book", produces = MediaType.APPLICATION_JSON_VALUE)
  Mono<Book> addNewBook(@RequestBody @Validated BookDto bookDto) {
    return MonoUtils.fromCallable(() -> bookRepository.save(bookDto.toDomain()))
        .doOnSubscribe(result -> log.info("Adding new book {}", bookDto.getTitle()));
  }

  @DeleteMapping(
      value = "/{bookId}",
      name = "Get book by id",
      produces = MediaType.APPLICATION_JSON_VALUE)
  Mono<Book> removeBookById(@PathVariable Long bookId) {
    return getBookById(bookId)
        .flatMap(this::deleteBook)
        .doOnSubscribe(result -> log.info("Deleting book id {}", bookId));
  }

  @GetMapping(
      value = "/search",
      name = "Search for books",
      produces = MediaType.APPLICATION_JSON_VALUE)
  Flux<Book> searchForBooks(@RequestParam String q) {
    return searchFor(bookRepository::searchForIdsByTitle, q)
        .mergeWith(searchFor(bookRepository::searchForIdsByAuthor, q))
        .doOnSubscribe(result -> log.info("Searching for books using query: {}", q));
  }

  private Mono<Book> deleteBook(Book b) {
    return MonoUtils.fromCallable(
        () -> {
          bookRepository.deleteById(b.getId());
          return b;
        });
  }

  private Flux<Book> searchFor(Function<String, List<Long>> search, String query) {
    var bookIds = search.apply(query.toUpperCase());
    if (bookIds.isEmpty()) return Flux.empty();
    return MonoUtils.retrieveAsList(() -> bookRepository.findAllById(bookIds));
  }
}
