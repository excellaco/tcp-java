package com.excella.reactor.controllers.advice;

import com.excella.reactor.common.exceptions.GenericError;
import com.excella.reactor.common.exceptions.ResourceNotFoundException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import lombok.extern.slf4j.Slf4j;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.transaction.TransactionSystemException;
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

    // Must be a LinkedHashSet else it is not guaranteed to collect to List in insertion order.
    Set<ConstraintViolation<?>> constraintViolations = new LinkedHashSet<>();
    constraintViolations.add(mockConstraintViolation("TEST1", "INVALID", "A"));
    constraintViolations.add(mockConstraintViolation("TEST2", "INVALID", false));

    var exception = Mockito.mock(ConstraintViolationException.class);
    Mockito.when(exception.getConstraintViolations()).thenReturn(constraintViolations);

    var actualError = advice.handleConstraintViolationException(exception);

    assert expectedError.equals(actualError);
  }

  @Test(
      description =
          "When the root cause is a ConstraintViolationException, "
              + "handleTransactionException should yield a response with a 409 Conflict Status "
              + "and containing a GenericError describing the constraint violation")
  public void testHandleTransactionExceptionWhenConstraintViolation() {
    var expectedErrorCodes = List.of(formatFieldRejection("TEST1", "INVALID", "A"));
    var expectedError =
        GenericError.builder()
            .message("Validation errors occurred")
            .code(HttpStatus.CONFLICT.value())
            .details(expectedErrorCodes)
            .build();

    var expectedResponse = new ResponseEntity<>(expectedError, HttpStatus.CONFLICT);

    var exception = Mockito.mock(TransactionSystemException.class);
    var rootCause = Mockito.mock(ConstraintViolationException.class);
    Mockito.when(exception.getRootCause()).thenReturn(rootCause);

    Set<ConstraintViolation<?>> constraintViolations =
        Set.of(mockConstraintViolation("TEST1", "INVALID", "A"));
    Mockito.when(rootCause.getConstraintViolations()).thenReturn(constraintViolations);

    var actualResponse = advice.handleTransactionException(exception);

    assert expectedResponse.equals(actualResponse);
  }

  @Test(
      description =
          "When the root cause is not a ConstraintViolationException, "
              + "handleTransactionException should yield a response with a 500 Internal Error Status "
              + "and containing a GenericError indicating an unknown error.")
  public void testHandleTransactionExceptionWhenOtherCause() {
    var expectedError =
        GenericError.builder()
            .message("An error occurred while processing the request")
            .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .build();

    var expectedResponse = new ResponseEntity<>(expectedError, HttpStatus.INTERNAL_SERVER_ERROR);

    var exception = Mockito.mock(TransactionSystemException.class);
    var rootCause = Mockito.mock(Exception.class);
    Mockito.when(exception.getRootCause()).thenReturn(rootCause);
    Mockito.when(exception.getMessage())
        .thenReturn("Test Exception"); // Due to side effects this is necessary

    var actualResponse = advice.handleTransactionException(exception);

    assert expectedResponse.equals(actualResponse);
  }

  @Test(
      description =
          "When the cause is a MismatchedInputException, "
              + "handleHttpNotReadableException should yield a response with a 400 Bad Request Status "
              + "and containing a GenericError exposing the cause's message")
  public void testHandleHttpNotReadableExceptionWhenMismatchedInput() {
    var expectedError =
        GenericError.builder()
            .message("One or more input values was in an unreadable format")
            .code(HttpStatus.BAD_REQUEST.value())
            .details(Collections.singletonList("TEST1"))
            .build();

    var expectedResponse = new ResponseEntity<>(expectedError, HttpStatus.BAD_REQUEST);

    var exception = Mockito.mock(HttpMessageNotReadableException.class);
    var cause = Mockito.mock(MismatchedInputException.class);
    Mockito.when(exception.getCause()).thenReturn(cause);

    Mockito.when(cause.getMessage()).thenReturn("TEST1");

    var actualResponse = advice.handleHttpNotReadableException(exception);

    assert expectedResponse.equals(actualResponse);
  }

  @Test(
      description =
          "When the cause is not a MismatchedInputException, "
              + "handleHttpNotReadableException should yield a response with a 400 Bad Request Status "
              + "and containing a GenericError indicating an unknown error")
  public void testHandleHttpNotReadableExceptionWhenOtherCause() {
    var expectedError =
        GenericError.builder()
            .message("An error occurred while processing the request")
            .code(HttpStatus.BAD_REQUEST.value())
            .build();

    var expectedResponse = new ResponseEntity<>(expectedError, HttpStatus.BAD_REQUEST);

    var exception = Mockito.mock(HttpMessageNotReadableException.class);
    var cause = Mockito.mock(Exception.class);
    Mockito.when(exception.getCause()).thenReturn(cause);

    var actualResponse = advice.handleHttpNotReadableException(exception);

    assert expectedResponse.equals(actualResponse);
  }

  @Test(
      description =
          "handleFallbackException should yield a GenericError with a 500 Internal Server Error Status "
              + "and indicating an unknown error")
  public void testHandleFallbackException() {
    var expectedError =
        GenericError.builder()
            .message("An error occurred while processing the request")
            .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .build();

    assert expectedError.equals(advice.handleFallbackException(new Exception()));
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
