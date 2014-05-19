package de.fisp.eetest.exception.handlers;

import de.fisp.eetest.exceptions.BaseException;
import de.fisp.eetest.exceptions.BusinessConstraintViolationException;
import de.fisp.eetest.exceptions.BusinessValidationException;
import de.fisp.eetest.exceptions.NotFoundException;
import de.fisp.eetest.exceptions.handler.BaseExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RestExceptionHandler implements BaseExceptionHandler<Response> {
  private Response response;
  private static final Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

  /**
   * Liefert einen Status Code 400 (BAD_REQUEST) und ein JSON-Objekt, dass alle Validierungsfehler
   * als (Feld, Wert)-Paare enthält.
   *
   * @param ex Die Exception mit den Validierungsfehlern
   * @return Der ExceptionHandler mit gesetzter Result.
   */
  @Override
  public BaseExceptionHandler<Response> handleBusinessConstraintViolationException(BusinessConstraintViolationException ex) {
    response = createViolationResponse(ex.getConstraintViolations());
    return this;
  }

  /**
   * Liefert einen Status Code 400 (BAD_REQUEST) und ein JSON-Objekt
   * mit <code>ex.getMessage()</code> als "message" Attribut.
   *
   * @param ex Die Exception mit der Validierungsfehlermeldung.
   * @return Der ExceptionHandler mit gesetzter Result.
   */
  @Override
  public BaseExceptionHandler<Response> handleBusinessValidationException(BusinessValidationException ex) {
    response = createValidationResponse(ex.getMessage());
    return this;
  }

  /**
   * Liefert einen Status Code 404 (NOT_FOUND) und ein JSON-Objekt
   * mit <code>ex.getMessage()</code> als "message" Attribut.
   *
   * @param ex Die Exception mit der Fehlermeldung
   * @return Der ExceptionHandler mit gesetzter Result.
   */
  @Override
  public BaseExceptionHandler<Response> handleNotFoundException(NotFoundException ex) {
    response = createNotFoundResponse(ex.getMessage());
    return this;
  }

  @Override
  public BaseExceptionHandler<Response> defaultHandleException(Exception exception) {
    if (exception instanceof WebApplicationException) {
      WebApplicationException webApplicationException = (WebApplicationException) exception;
      response = webApplicationException.getResponse();
    } else if (exception.getCause() instanceof WebApplicationException) {
      /*
      * Wenn die Exception aus einer EJB kommt, ist sie immer in eine EJBException
      * (oder eine Unterklasse von EJBException) eingepackt.
       */
      WebApplicationException webApplicationException = (WebApplicationException) exception.getCause();
      response = webApplicationException.getResponse();
    } else {
      /*
       * Logging, da sonst Nullpointer Exceptions etc. nicht im Log auftauchen
       */
      logger.error(exception.getMessage(), exception);
      response = createExceptionResponse(exception);
    }

    return this;
  }

  /**
   * Behandelt Standardexceptions, d.h. alle Exceptions, die nicht weiter spezifiziert sind.
   * Das Ergebnis hat den HTTP-Status-Code 500 (Internal Server Error). Die Exception wird
   * mit Stacktrace gelogged.
   *
   * @param ex Die zu behandelnde Exception
   * @return Der ExceptionHandler mit gesetzter Result.
   */
  @Override
  public BaseExceptionHandler<Response> handleBaseException(BaseException ex) {
    response = createExceptionResponse(ex);
    return this;
  }

  @Override
  public Response getResult() {
    return response;
  }

  private Response createViolationResponse(Set<ConstraintViolation<?>> violations) {
    logger.info("Validation completed. violations found: " + violations.size());
    Map<String, String> responseObj = new HashMap<String, String>();
    for (ConstraintViolation<?> violation : violations) {
      responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
    }
    return Response.status(Response.Status.BAD_REQUEST).entity(responseObj).build();
  }

  private Response createNotFoundResponse(String message) {
    return Response.status(Response.Status.NOT_FOUND).entity(createMessage(message)).build();
  }

  private Response createValidationResponse(String message) {
    return Response.status(Response.Status.BAD_REQUEST).entity(createMessage(message)).build();
  }

  private Response createExceptionResponse(Exception e) {
    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(createMessage(e.getMessage())).build();
  }

  private Map<String, String> createMessage(String message) {
    Map<String, String> result = new HashMap<>();
    result.put("message", message);
    return result;
  }
}
