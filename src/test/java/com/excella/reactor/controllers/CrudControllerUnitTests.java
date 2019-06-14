package com.excella.reactor.controllers;

import com.excella.reactor.common.exceptions.ResourceNotFoundException;
import com.excella.reactor.domain.DomainModel;
import com.excella.reactor.service.CrudService;
import java.util.Arrays;
import javax.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.data.repository.CrudRepository;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@Test
public class CrudControllerUnitTests {
  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  @Entity
  private class SampleEntity extends DomainModel {}

  private class SampleService implements CrudService {

    public CrudRepository getRepository() {
      return Mockito.mock(CrudRepository.class);
    }
  }

  private CrudService mockService;

  private class SampleCrudController extends CrudController<SampleEntity> {

    CrudService<SampleEntity> getService() {
      return mockService;
    }
  }

  private SampleCrudController testController;
  private SampleEntity mockEntity1 = new SampleEntity();
  private SampleEntity mockEntity2 = new SampleEntity();
  private SampleEntity mockEntity3 = new SampleEntity();

  @BeforeMethod
  private void runBeforeEachMethod() {
    testController = new SampleCrudController();
    mockService = Mockito.mock(CrudService.class);
  }

  @AfterMethod
  private void runAfterEachMethod() {
    Mockito.reset(mockService);
  }

  @Test
  private void getAll_can_return_flux_with_multiple_entities() {
    Mockito.when(mockService.all()).thenReturn(Flux.just(mockEntity1, mockEntity2, mockEntity3));
    StepVerifier.create(testController.getAll())
        .expectNextSequence(Arrays.asList(mockEntity1, mockEntity2, mockEntity3))
        .expectComplete()
        .verify();
  }

  @Test
  private void getAll_can_return_empty_flux() {
    Mockito.when(mockService.all()).thenReturn(Flux.empty());

    StepVerifier.create(testController.getAll()).expectComplete().verify();
  }

  // .byId() Throws ResourceNotFoundException if nothing found

  @Test
  private void byId_throws_ResourceNotFoundException_if_nothing_found() {
    Mockito.when(mockService.byId(Mockito.anyLong())).thenReturn(Mono.empty());
    StepVerifier.create(testController.getById(1234L))
        .expectError(ResourceNotFoundException.class)
        .verify();
  }

  // .byId() returns an entity if one is found
  @Test
  private void byId_returns_entity_if_found() {
    Mockito.when(mockService.byId(Mockito.anyLong())).thenReturn(Mono.just(mockEntity1));
    StepVerifier.create(testController.getById(1234L))
        .expectNext(mockEntity1)
        .expectComplete()
        .verify();
  }

  @Test
  private void create_calls_save_and_returns_newly_created_entity() {
    Mockito.when(mockService.save(mockEntity1)).thenReturn(Mono.just(mockEntity1));
    StepVerifier.create(testController.create(mockEntity1))
        .expectNext(mockEntity1)
        .expectComplete()
        .verify();
    Mockito.verify(mockService, Mockito.times(1)).save(ArgumentMatchers.eq(mockEntity1));
  }

  @Test
  private void update_calls_update_on_service_and_returns_updated_item() {
    Mockito.when(mockService.update(123L, mockEntity2)).thenReturn(Mono.just(mockEntity2));
    StepVerifier.create(testController.update(123L, mockEntity2))
        .expectNext(mockEntity2)
        .expectComplete()
        .verify();

    Mockito.verify(mockService, Mockito.times(1))
        .update(ArgumentMatchers.eq(123L), ArgumentMatchers.eq(mockEntity2));
  }

  @Test
  private void removeById_calls_delete_on_service_and_returns_deleted_item() {
    Mockito.when(mockService.delete(123L)).thenReturn(Mono.just(mockEntity2));
    StepVerifier.create(testController.removeById(123L)).expectNext(mockEntity2).verifyComplete();

    Mockito.verify(mockService, Mockito.times(1)).delete(ArgumentMatchers.eq(123L));
  }
}
