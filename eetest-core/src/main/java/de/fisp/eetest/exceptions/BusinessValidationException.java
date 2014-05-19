package de.fisp.eetest.exceptions;

import de.fisp.eetest.exceptions.handler.BaseExceptionHandler;

import javax.ejb.ApplicationException;

/**
 * Diese Exception wird bei allgemeinen Validierungsfehlern geworfen.
 *
 * <code>
 *   if(!verifyEmailAddress(emailAddress))
 *      throw new BusinessValidationException("Email address is not reachable");
 * </code>
 */
@ApplicationException(rollback=true)
public class BusinessValidationException extends BaseException {
  public BusinessValidationException() {
  }

  public BusinessValidationException(String message) {
    super(message);
  }

  public BusinessValidationException(String message, Throwable cause) {
    super(message, cause);
  }

  public BusinessValidationException(Throwable cause) {
    super(cause);
  }

  public BusinessValidationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public <T> T handle(BaseExceptionHandler<T> handler) {
    return handler.handleBusinessValidationException(this).getResult();
  }
}
