package de.fisp.eetest.exceptionmappers;

import de.fisp.eetest.exception.handlers.RestExceptionHandler;
import de.fisp.eetest.exceptions.BaseException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Sorgt dafür, dass alle nicht abgefangenen Exceptions in REST-Services als JSON zurückgeliefert werden
 */
@Provider
public class AnyExceptionMapper implements ExceptionMapper<Exception> {
  @Override
  public Response toResponse(Exception exception) {
    if (exception instanceof BaseException) {
     /*
      * Anwendungsspezifische Exceptions werden anwendungsspezifisch behandelt.
      */
      RestExceptionHandler handler = new RestExceptionHandler();
      BaseException ex = (BaseException) exception;
      return ex.handle(handler);
    } else {
      /*
       * Alle anderen Exceptions bekommen eine Standardbehandlung
       */
      RestExceptionHandler handler = new RestExceptionHandler();
      handler.defaultHandleException(exception);
      return handler.getResult();
    }
  }
}
