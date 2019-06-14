package com.excella.reactor.controllers;

import com.excella.reactor.domain.Skill;
import com.excella.reactor.service.CrudService;
import com.excella.reactor.service.SkillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/skill")
@Slf4j
public class SkillController extends CrudController<Skill> {
  private SkillService service;

  @Autowired
  public SkillController(SkillService service) {
    this.service = service;
  }

  CrudService<Skill> getService() {
    return this.service;
  }
}
