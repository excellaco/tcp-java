package com.excella.reactor.controllers.advice;

import com.excella.reactor.common.exceptions.GenericError;
import com.excella.reactor.common.exceptions.ResourceNotFoundException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

/**
 * ControllerAdvice Component used to handle exceptions. Currently applies globally, but could be
 * targeted more specifically using the annotation's arguments.
 */
@ControllerAdvice
@Slf4j
@RequestMapping
public class CrudControllerAdvice {

  /**
   * Handles custom 404 Exception. We typically wrap these into the response {@code Publisher} when
   * the repository returns no resource for a query by ID, and they are thrown when the subscription
   * fires.
   *
   * @param e the {@code ResourceNotFoundException} being handled
   * @return a {@code GenericError} with the message of the 404 Exception
   * @see com.excella.reactor.service.CrudService#byId(Long)
   */
  @ExceptionHandler(ResourceNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ResponseBody
  GenericError handleResourceNotFoundException(final ResourceNotFoundException e) {
    log.warn(e.getMessage(), e.getCause());
    return buildGenericError(e.getMessage(), HttpStatus.NOT_FOUND, null);
  }

  /**
   * Handles exceptions caused by integrity constraint violations. Typically indicative of an
   * attempt to insert a value with non-unique primary key.
   *
   * <p>One instance where this could occur, even with generated primary keys, is if an insert is
   * cascaded to a join table, and the composite key is not unique.
   *
   * @param e the {@code DataIntegrityViolationException} being handled
   * @return a {@code GenericError} indicating the conflict
   */
  @ExceptionHandler(DataIntegrityViolationException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  @ResponseBody
  GenericError handleDataIntegrityViolation(final DataIntegrityViolationException e) {
    log.warn(e.getMessage(), e.getCause());
    return buildGenericError(
        "The resource could not be persisted or altered as specified", HttpStatus.CONFLICT, null);
  }

  /**
   * Handles validation exceptions typically triggered by a model being checked with {@code @Valid}
   * or {@code @Validated}.
   *
   * @param e the {@code MethodArgumentNotValidException} being handled
   * @return a {@code GenericError} holding formatted details about the fields failing validation
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  GenericError handleRequestValidationException(final MethodArgumentNotValidException e) {
    log.warn(e.getMessage(), e.getCause());
    return buildGenericError(
        "Validation errors occurred",
        HttpStatus.BAD_REQUEST, // Consider HttpStatus.UNPROCESSABLE_ENTITY
        e.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(CrudControllerAdvice::formatFieldError)
            .collect(Collectors.toUnmodifiableList()));
  }

  /**
   * Handles a constraint violation exception. These are typically triggered by an internal
   * constraint validation or a path variable being checked, as opposed to a model being checked
   * with {@code @Valid} or {@code @Validated}.
   *
   * @param e the {@code ConstraintViolationException} being handled
   * @return a {@code GenericError} holding formatted details about the constraint violations
   */
  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  GenericError handleConstraintViolationException(final ConstraintViolationException e) {
    log.warn(e.getMessage(), e.getCause());
    return buildGenericError(
        "Validation errors occurred",
        HttpStatus.BAD_REQUEST, // Consider HttpStatus.UNPROCESSABLE_ENTITY
        e.getConstraintViolations()
            .stream()
            .map(CrudControllerAdvice::formatConstraintViolation)
            .collect(Collectors.toUnmodifiableList()));
  }

  /**
   * Handles an exception generated at transaction time. Typically this is something that caused a
   * rollback, e.g., an invalid foreign key value being used on a cascaded insert (results in null).
   *
   * @param e The TransactionSystemException being handled
   * @return If caused by constraint violations, a {@code 409 Conflict} response listing those
   *     violations; otherwise, an internal error response.
   */
  @ExceptionHandler(TransactionSystemException.class)
  ResponseEntity<GenericError> handleTransactionException(final TransactionSystemException e) {
    if (e.getRootCause() instanceof ConstraintViolationException) {
      log.warn(e.getMessage(), e.getCause());

      return new ResponseEntity<>(
          handleConstraintViolationException((ConstraintViolationException) e.getRootCause()),
          HttpStatus.CONFLICT);
    }

    return new ResponseEntity<>(handleFallbackException(e), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  /**
   * Handles errors that most commonly occur when transforming the HTTP request to the RequestBody
   * object. Most commonly seen when the value of a field does not match the type expected on the
   * receiving object. One example is an invalid value being de-serialized to an enum-typed field.
   *
   * @param e the {@code HttpMessageNotReadableException} being handled
   * @return a response indicating a {@literal 400 Bad Request}, including details about the
   *     offending field if possible
   */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  ResponseEntity<GenericError> handleHttpNotReadableException(
      final HttpMessageNotReadableException e) {
    if (e.getCause() instanceof MismatchedInputException) {
      log.warn(e.getMessage(), e.getCause());
      return new ResponseEntity<>(
          buildGenericError(
              "One or more input values was in an unreadable format",
              HttpStatus.BAD_REQUEST,
              Collections.singletonList(e.getCause().getMessage())),
          HttpStatus.BAD_REQUEST);
    }

    /* Errors reaching this statement would still almost certainly be the client's fault,
    so we should still send a 400 status code. */
    return new ResponseEntity<>(handleFallbackException(e), HttpStatus.BAD_REQUEST);
  }

  /**
   * Fallback handler for all exceptions. Does not expose details of the exception on the response
   * object.
   *
   * <p>In contrast with other handlers, this will log the class of the exception. The intent is to
   * inform future implementation of more specific handling.
   *
   * @param e the {@code Exception} being handled
   * @return a {@code GenericError} representing an Internal Server Error with no details exposed.
   */
  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  GenericError handleFallbackException(final Exception e) {
    log.warn(e.getClass().getSimpleName() + ": " + e.getMessage(), e.getCause());
    return buildGenericError(
        "An error occurred while processing the request", HttpStatus.INTERNAL_SERVER_ERROR, null);
  }

  private static GenericError buildGenericError(
      String message, HttpStatus httpStatus, List<String> details) {
    return GenericError.builder()
        .message(message)
        .code(httpStatus.value())
        .details(details)
        .build();
  }

  private static String formatFieldError(FieldError fieldError) {
    return fieldError.getField()
        + ": "
        + fieldError.getDefaultMessage()
        + " (Rejected value: "
        + fieldError.getRejectedValue()
        + ")";
  }

  private static String formatConstraintViolation(ConstraintViolation violation) {
    return violation.getPropertyPath()
        + ": "
        + violation.getMessage()
        + " (Rejected value: "
        + violation.getInvalidValue()
        + ")";
  }
}
