package com.fabricio.campsite.exceptions;

import java.util.ArrayList;
import java.util.List;
import javax.validation.UnexpectedTypeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler({MethodArgumentNotValidException.class, UnexpectedTypeException.class})
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  protected Issue handleMethodArgumentNotValid(
      final MethodArgumentNotValidException ex) {
    final List<String> errors = new ArrayList<>();
    for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
      errors.add(error.getField() + ": " + error.getDefaultMessage());
    }
    for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
      errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
    }
    return new Issue(IssueEnum.MALFORMED_REQUEST, errors);
  }

  @ExceptionHandler(BadRequestException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  protected Issue processBadRequestException(final GlobalException ex) {
    return ex.getIssue();
  }

  @ExceptionHandler({NotFoundException.class})
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  protected Issue processNotFoundException(final GlobalException ex) {
    return ex.getIssue();
  }

}
