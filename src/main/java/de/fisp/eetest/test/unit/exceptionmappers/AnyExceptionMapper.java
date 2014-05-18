package de.fisp.eetest.test.unit.exceptionmappers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

/**
 * Sorgt dafür, dass alle nicht abgefangenen Exceptions in REST-Services als JSON zurückgeliefert werden
 */
@Provider
public class AnyExceptionMapper implements ExceptionMapper<Exception> {
  private static final Logger log = LoggerFactory.getLogger(AnyExceptionMapper.class);

  @Override
  public Response toResponse(Exception exception) {
    /*
     * instanceof, da hier Exceptions behandelt werden, die Applicationsserverspezifisch sind.
     * Bsp.: Exceptions aus dne Paketen org.resteasy und org.codehaus beim JBOSS
     *
     * Alle REST-Exceptions (WebApplicationExceptions) werden sowieso schon als JSON ausgegeben,
     * deshalb die Sonderbehandlung.
     */
    if (exception instanceof WebApplicationException) {
      WebApplicationException webApplicationException = (WebApplicationException) exception;
      return webApplicationException.getResponse();
    } else if (exception.getCause() instanceof WebApplicationException) {
      /*
      * Wenn die Exception aus einer EJB kommt, ist sie immer in eine EJBException
      * (oder eine Unterklasse von EJBException) eingepackt.
       */
      WebApplicationException webApplicationException = (WebApplicationException) exception.getCause();
      return webApplicationException.getResponse();
    } else {
      /*
       * Logging, da sonst Nullpointer Exceptions etc. nicht im Log auftauchen
       */
      log.error(exception.getMessage(), exception);
      Map<String, String> messageMap = new HashMap<>();
      messageMap.put("message", exception.getMessage());
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(messageMap).build();
    }
  }
}
