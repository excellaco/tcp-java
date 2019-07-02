package com.excella.reactor.service;

import com.excella.reactor.common.exceptions.ResourceNotFoundException;
import com.excella.reactor.common.reactor.MonoUtils;
import com.excella.reactor.domain.DomainModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CrudService<T extends DomainModel> {

  /**
   * gets all objects of type T from the database
   *
   * @param pageable pagination information
   * @return all database objects of type T
   */
  default Flux<T> all(Pageable pageable) {
    return MonoUtils.retrieveAsList(() -> getRepository().findAll(pageable));
  }

  /**
   * Gets a an object of type T from the database by the object's id.
   *
   * @param id id of the crud object
   * @return Mono object of type T
   */
  default Mono<T> byId(Long id) {
    return MonoUtils.fromCallableOpt(() -> getRepository().findById(id))
        .switchIfEmpty(
            Mono.error(ResourceNotFoundException.of("Resource with id " + id + " not found")));
  }

  /**
   * saves an object in the database
   *
   * @param t an object of type t
   * @return a Mono of the same object
   */
  default Mono<T> save(T t) {
    return MonoUtils.fromCallable(() -> getRepository().save(t));
  }

  /**
   * looks up database object by id and updates fields saves object if one with the id does not
   * already exist
   *
   * @param id id of the database object to update
   * @param t object with the new values
   * @return a Mono of the updated object
   */
  default Mono<T> update(Long id, T t) {
    return byId(id)
        .map(
            p -> {
              t.setId(id);
              return t;
            })
        .flatMap(this::save);
  }

  /**
   * deletes an object of type T from the database by the object's id
   *
   * @param id id of the database object to delete
   * @return a Mono of the deleted object
   */
  default Mono<T> delete(Long id) {
    return byId(id)
        .flatMap(
            p ->
                MonoUtils.fromCallable(
                    () -> {
                      getRepository().delete(p);
                      return p;
                    }));
  }

  /**
   * Gets the JPA repository for this service
   *
   * @return JPA repository
   */
  JpaRepository<T, Long> getRepository();
}
