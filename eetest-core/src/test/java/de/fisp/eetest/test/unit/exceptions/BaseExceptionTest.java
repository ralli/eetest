package de.fisp.eetest.test.unit.exceptions;

import de.fisp.eetest.exceptions.BaseException;
import de.fisp.eetest.exceptions.BusinessConstraintViolationException;
import de.fisp.eetest.exceptions.BusinessValidationException;
import de.fisp.eetest.exceptions.NotFoundException;
import de.fisp.eetest.exceptions.handler.BaseExceptionHandler;
import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BaseExceptionTest {
  private enum ExceptionType {
    BUSINESS_CONSTRAINT, BUSINESS_VALIDATION, NOT_FOUND, BASE, GENERAL
  }

  private class TestExceptionHandler implements BaseExceptionHandler<ExceptionType> {
    ExceptionType result;

    @Override
    public BaseExceptionHandler<ExceptionType> handleBusinessConstraintViolationException(BusinessConstraintViolationException ex) {
      result = ExceptionType.BUSINESS_CONSTRAINT;
      return this;
    }

    @Override
    public BaseExceptionHandler<ExceptionType> handleBusinessValidationException(BusinessValidationException ex) {
      result = ExceptionType.BUSINESS_VALIDATION;
      return this;
    }

    @Override
    public BaseExceptionHandler<ExceptionType> handleNotFoundException(NotFoundException ex) {
      result = ExceptionType.NOT_FOUND;
      return this;
    }

    @Override
    public BaseExceptionHandler<ExceptionType> handleBaseException(BaseException ex) {
      result = ExceptionType.BASE;
      return this;
    }

    @Override
    public BaseExceptionHandler<ExceptionType> defaultHandleException(Exception ex) {
      result = ExceptionType.GENERAL;
      return this;
    }

    @Override
    public ExceptionType getResult() {
      return result;
    }
  }

  private TestExceptionHandler exceptionHandler;

  @Before
  public void initialize() {
    exceptionHandler = new TestExceptionHandler();
  }

  @Test
  public void BusinessConstraintViolationException_handle_should_return_BUSINESS_CONSTRAINT() {
    Set<ConstraintViolation<?>> violations = new HashSet<>();
    violations.add(createConstraintViolation("field", "message"));
    assertEquals(ExceptionType.BUSINESS_CONSTRAINT, new BusinessConstraintViolationException(violations).handle(exceptionHandler));
  }

  @Test
  public void BusinessValidationException_handle_should_return_BUSINESS_VALIDATION() {
    assertEquals(ExceptionType.BUSINESS_VALIDATION, new BusinessValidationException("Test").handle(exceptionHandler));
  }

  @Test
  public void NotFoundException_handle_should_return_NOT_FOUND() {
    assertEquals(ExceptionType.NOT_FOUND, new NotFoundException("Test").handle(exceptionHandler));
  }

  private <T> ConstraintViolation<T> createConstraintViolation(String field, String message) {
    @SuppressWarnings("unchecked")
    ConstraintViolation<T> violation = mock(ConstraintViolation.class);
    Path path = mock(Path.class);
    when(path.toString()).thenReturn(field);
    when(violation.getPropertyPath()).thenReturn(path);
    when(violation.getMessage()).thenReturn(message);
    return violation;
  }
}