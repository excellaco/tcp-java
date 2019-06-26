package com.excella.reactor.service;

import com.excella.reactor.common.exceptions.ResourceNotFoundException;
import com.excella.reactor.common.reactor.MonoUtils;
import com.excella.reactor.domain.DomainModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CrudService<T extends DomainModel> {
  default Flux<T> all(Pageable pageable) {
    return MonoUtils.retrieveAsList(() -> getRepository().findAll(pageable));
  }

  default Mono<T> byId(Long id) {
    return MonoUtils.fromCallableOpt(() -> getRepository().findById(id))
        .switchIfEmpty(
            Mono.error(ResourceNotFoundException.of("Resource with id " + id + " not found")));
  }

  default Mono<T> save(T t) {
    return MonoUtils.fromCallable(() -> getRepository().save(t));
  }

  default Mono<T> update(Long id, T t) {
    return byId(id)
        .map(
            p -> {
              t.setId(id);
              return t;
            })
        .flatMap(this::save);
  }

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

  JpaRepository<T, Long> getRepository();
}
