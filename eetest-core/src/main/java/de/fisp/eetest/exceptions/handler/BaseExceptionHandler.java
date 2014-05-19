package de.fisp.eetest.exceptions.handler;

import de.fisp.eetest.exceptions.BaseException;
import de.fisp.eetest.exceptions.BusinessConstraintViolationException;
import de.fisp.eetest.exceptions.BusinessValidationException;
import de.fisp.eetest.exceptions.NotFoundException;

public interface BaseExceptionHandler<T> {
  BaseExceptionHandler<T> handleBusinessConstraintViolationException(BusinessConstraintViolationException ex);
  BaseExceptionHandler<T> handleBusinessValidationException(BusinessValidationException ex);
  BaseExceptionHandler<T> handleNotFoundException(NotFoundException ex);
  BaseExceptionHandler<T> handleBaseException(BaseException ex);
  BaseExceptionHandler<T> defaultHandleException(Exception ex);
  T getResult();
}
