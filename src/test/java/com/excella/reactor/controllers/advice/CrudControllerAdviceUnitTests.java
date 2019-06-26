package com.excella.reactor.controllers.advice;

import com.excella.reactor.common.exceptions.GenericError;
import com.excella.reactor.common.exceptions.ResourceNotFoundException;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import lombok.extern.slf4j.Slf4j;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

@Test
@Slf4j
public class CrudControllerAdviceUnitTests {

  private CrudControllerAdvice advice;

  @BeforeTest
  public void runBeforeTests() {
    advice = new CrudControllerAdvice();
  }

  @Test(
      description =
          "handleResourceNotFoundException should yield a GenericError with the exception's message and 404 Status Code")
  public void testHandleResourceNotFoundException() {
    var expectedError =
        GenericError.builder().message("TEST").code(HttpStatus.NOT_FOUND.value()).build();

    var actualError = advice.handleResourceNotFoundException(new ResourceNotFoundException("TEST"));

    assert expectedError.equals(actualError);
  }

  @Test(
      description =
          "handleDataIntegrityViolation should yield a GenericError with a fixed message and 409 Conflict Status Code")
  public void testHandleDataIntegrityViolation() {
    var expectedError =
        GenericError.builder()
            .message("The resource could not be persisted or altered as specified")
            .code(HttpStatus.CONFLICT.value())
            .build();

    var actualError = advice.handleDataIntegrityViolation(new DataIntegrityViolationException(""));

    assert expectedError.equals(actualError);
  }

  @Test(
      description =
          "handleRequestValidationException should yield a GenericError with a fixed message, "
              + "400 Bad Request Status Code, and list of formatted field errors")
  public void testHandleRequestValidationException() {
    var expectedErrorCodes =
        List.of(
            formatFieldRejection("TEST1", "INVALID", "A"),
            formatFieldRejection("TEST2", "INVALID", false));

    var expectedError =
        GenericError.builder()
            .message("Validation errors occurred")
            .code(HttpStatus.BAD_REQUEST.value())
            .details(expectedErrorCodes)
            .build();

    var bindingResult = Mockito.mock(BindingResult.class);
    Mockito.when(bindingResult.getFieldErrors())
        .thenReturn(
            List.of(
                new FieldError("", "TEST1", "A", false, null, null, "INVALID"),
                new FieldError("", "TEST2", false, false, null, null, "INVALID")));

    var exception = Mockito.mock(MethodArgumentNotValidException.class);
    Mockito.when(exception.getBindingResult()).thenReturn(bindingResult);
    Mockito.when(exception.getMessage())
        .thenReturn("Test Exception"); // Due to side effects this is necessary

    var actualError = advice.handleRequestValidationException(exception);

    assert expectedError.equals(actualError);
  }

  @Test(
      description =
          "handleConstraintViolationException should yield a GenericError with a fixed message, "
              + "400 Bad Request Status Code, and list of formatted constraint violations")
  public void testHandleConstraintViolationException() {
    var expectedErrorCodes =
        List.of(
            formatFieldRejection("TEST1", "INVALID", "A"),
            formatFieldRejection("TEST2", "INVALID", false));

    var expectedError =
        GenericError.builder()
            .message("Validation errors occurred")
            .code(HttpStatus.BAD_REQUEST.value())
            .details(expectedErrorCodes)
            .build();

    var constraintViolations =
        Set.of(
            mockConstraintViolation("TEST1", "INVALID", "A"),
            mockConstraintViolation("TEST2", "INVALID", false));
    var exception = Mockito.mock(ConstraintViolationException.class);
    Mockito.when(exception.getConstraintViolations()).thenReturn(constraintViolations);
    Mockito.when(exception.getMessage())
        .thenReturn("Test Exception"); // Due to side effects this is necessary

    var actualError = advice.handleConstraintViolationException(exception);

    assert expectedError.equals(actualError);
  }

  private static String formatFieldRejection(
      String fieldName, String rejectionMessage, Object invalidValue) {
    final String format = "%s: %s (Rejected value: %s)";

    return String.format(format, fieldName, rejectionMessage, invalidValue);
  }

  private static ConstraintViolation<?> mockConstraintViolation(
      String propertyPath, String rejectionMessage, Object invalidValue) {
    var violation = Mockito.mock(ConstraintViolation.class);
    var path = Mockito.mock(Path.class);
    Mockito.when(path.toString()).thenReturn(propertyPath);

    Mockito.when(violation.getPropertyPath()).thenReturn(path);
    Mockito.when(violation.getMessage()).thenReturn(rejectionMessage);
    Mockito.when(violation.getInvalidValue()).thenReturn(invalidValue);
    return violation;
  }
}
