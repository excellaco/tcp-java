package com.excella.reactor.service.impl;

import com.excella.reactor.domain.SkillCategory;
import com.excella.reactor.repositories.SkillCategoryRepository;
import com.excella.reactor.service.SkillCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
public class SkillCategoryServiceImpl implements SkillCategoryService {
  private SkillCategoryRepository repository;

  @Autowired
  public SkillCategoryServiceImpl(SkillCategoryRepository repository) {
    this.repository = repository;
  }

  @Override
  public CrudRepository<SkillCategory, Long> getRepository() {
    return repository;
  }
}
