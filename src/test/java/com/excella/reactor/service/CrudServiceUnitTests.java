package com.excella.reactor.service;

import com.excella.reactor.shared.SampleEntity;
import org.mockito.Mockito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CrudServiceUnitTests {

  JpaRepository<SampleEntity, Long> mockRepository;

  private class SampleCrudService implements CrudService<SampleEntity> {

    @Override
    public JpaRepository<SampleEntity, Long> getRepository() {
      return mockRepository;
    }
  }

  private SampleCrudService sampleService;
  private SampleEntity sampleEntity1 = new SampleEntity();
  private SampleEntity sampleEntity2 = new SampleEntity();
  private SampleEntity sampleEntity3 = new SampleEntity();
  private List<SampleEntity> sampleEntityList;

  @BeforeMethod
  private void beforeEach() {
    sampleService = new SampleCrudService();
    mockRepository = Mockito.mock(JpaRepository.class);
    sampleEntityList = Arrays.asList(sampleEntity1, sampleEntity2, sampleEntity3);
  }

  @AfterMethod
  private void afterEach() {
    Mockito.reset(mockRepository);
  }

  // all
  @Test
  private void all_method_can_return_empty_flux() {
    Mockito.when(mockRepository.findAll()).thenReturn(new ArrayList<>());
    StepVerifier.create(sampleService.all()).verifyComplete();
  }

  @Test
  private void all_method_can_return_flux_with_multiple_entities() {
    Mockito.when(mockRepository.findAll())
        .thenReturn(sampleEntityList);
    StepVerifier.create(sampleService.all())
        .expectNextSequence(sampleEntityList)
        .verifyComplete();
  }

  // byId

  @Test
  private void byId_can_return_nothing_when_no_matching_instance_found() {
    Mockito.when(mockRepository.findById(Mockito.anyLong())).thenReturn(null);
    StepVerifier.create(sampleService.byId(1234L)).verifyComplete();
  }

  @Test
  private void byId_can_return_instance_when_one_found() {

  }
  // save

  // update

  // delete

}
