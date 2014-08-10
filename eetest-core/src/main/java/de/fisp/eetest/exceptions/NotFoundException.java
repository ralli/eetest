package de.fisp.eetest.exceptions;

import de.fisp.eetest.exceptions.handler.BaseExceptionHandler;


public class NotFoundException extends BaseException {
  public NotFoundException(String message) {
    super(message);
  }

  public <T> T handle(BaseExceptionHandler<T> handler) {
    return handler.handleNotFoundException(this).getResult();
  }
}
