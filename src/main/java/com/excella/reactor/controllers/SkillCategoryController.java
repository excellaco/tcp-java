package com.excella.reactor.controllers;

import com.excella.reactor.domain.SkillCategory;
import com.excella.reactor.service.CrudService;
import com.excella.reactor.service.SkillCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/skill-category")
@Slf4j
public class SkillCategoryController extends CrudController<SkillCategory> {
  private SkillCategoryService service;

  @Autowired
  public SkillCategoryController(SkillCategoryService service) {
    this.service = service;
  }

  CrudService<SkillCategory> getService() {
    return this.service;
  }
}
