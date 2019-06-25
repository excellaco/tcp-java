package com.excella.reactor.controllers;

import com.excella.reactor.domain.DomainModel;
import com.excella.reactor.service.CrudService;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
public abstract class CrudController<T extends DomainModel> {
  @GetMapping(value = "/", name = "Get all of resource", produces = "application/json")
  Publisher<T> getAll(Pageable pageable) {
    return getService().all(pageable).doOnSubscribe(result -> log.info("Getting all"));
  }

  @GetMapping(value = "/{id}", name = "Get resource item by id", produces = "application/json")
  Publisher<T> getById(@PathVariable Long id) {
    return getService().byId(id).doOnSubscribe(result -> log.info("Getting id {}", id));
  }

  @PostMapping(value = "/", name = "Add a new resource item", produces = "application/json")
  Publisher<T> create(@RequestBody @Validated T t) {
    return getService()
        .save(t)
        .doOnSubscribe(result -> log.info("Adding new item {}", t.toString()));
  }

  @PutMapping(value = "/{id}", name = "Update a resource by id", produces = "application/json")
  Publisher<T> update(@PathVariable Long id, @RequestBody @Validated T t) {
    return getService().update(id, t).doOnSubscribe(result -> log.info("Updating item {}", id));
  }

  @DeleteMapping(value = "/{id}", name = "Delete resource by id", produces = "application/json")
  Publisher<T> removeById(@PathVariable Long id) {
    return getService().delete(id).doOnSubscribe(result -> log.info("Deleting id {}", id));
  }

  abstract CrudService<T> getService();
}
