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

@ControllerAdvice
@Slf4j
@RequestMapping
public class CrudControllerAdvice {

  @ExceptionHandler(ResourceNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ResponseBody
  GenericError handleResourceNotFoundException(final ResourceNotFoundException e) {
    log.warn(e.getMessage(), e.getCause());
    return buildGenericError("Resource not found", HttpStatus.NOT_FOUND, null);
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  @ResponseBody
  GenericError handleDataIntegrityViolation(final DataIntegrityViolationException e) {
    log.warn(e.getMessage(), e.getCause());
    return buildGenericError(
        "The resource could not be persisted or altered as specified", HttpStatus.CONFLICT, null);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  GenericError handleRequestValidationException(final MethodArgumentNotValidException e) {
    log.warn(e.getMessage(), e.getCause());
    return buildGenericError(
        "Validation errors occurred",
        HttpStatus.BAD_REQUEST,
        e.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(CrudControllerAdvice::formatFieldError)
            .collect(Collectors.toUnmodifiableList()));
  }

  /**
   * @param e
   * @return
   */
  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  GenericError handleConstraintViolationException(final ConstraintViolationException e) {
    log.warn(e.getMessage(), e.getCause());
    return buildGenericError(
        "Validation errors occurred",
        HttpStatus.BAD_REQUEST,
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
   * @return If caused by constraint violations, a response listing those violations; otherwise, an
   *     internal error response.
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

    return new ResponseEntity<>(handleFallbackException(e), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  GenericError handleFallbackException(final Exception e) {
    log.warn(e.getClass().getSimpleName() + ": " + e.getMessage(), e.getCause());
    return buildGenericError("An internal error occurred", HttpStatus.INTERNAL_SERVER_ERROR, null);
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
