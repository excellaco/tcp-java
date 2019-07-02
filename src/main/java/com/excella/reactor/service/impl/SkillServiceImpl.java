package com.excella.reactor.service.impl;

import com.excella.reactor.domain.Skill;
import com.excella.reactor.repositories.SkillRepository;
import com.excella.reactor.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class SkillServiceImpl implements SkillService {
  private SkillRepository repository;

  @Autowired
  public SkillServiceImpl(SkillRepository repository) {
    this.repository = repository;
  }

  @Override
  public JpaRepository<Skill, Long> getRepository() {
    return repository;
  }
}
