package de.fisp.eetest.exceptions;

import de.fisp.eetest.exceptions.handler.BaseExceptionHandler;

/**
 * Die Basis aller Exceptions, die von unserem Applikationscode geworfen werden.
 */
public abstract class BaseException extends RuntimeException {
  public BaseException() {
  }

  public BaseException(String message) {
    super(message);
  }

  public BaseException(String message, Throwable cause) {
    super(message, cause);
  }

  public BaseException(Throwable cause) {
    super(cause);
  }

  public BaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public <T> T handle(BaseExceptionHandler<T> handler) {
    return handler.handleBaseException(this).getResult();
  }
}
