package de.fisp.eetest.exceptionmappers;

import de.fisp.eetest.test.util.TestRuntimeDelegate;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.RuntimeDelegate;

import java.util.Map;

import static org.junit.Assert.*;

public class AnyExceptionMapperTest {
  private AnyExceptionMapper anyExceptionMapper;

  @Before
  public void initialize() {
    RuntimeDelegate.setInstance(new TestRuntimeDelegate());
    anyExceptionMapper = new AnyExceptionMapper();
  }

  @Test
  public void toResponse_should_return_the_original_json_if_a_WebApplicationException_is_passed() {
    Response response = Response.ok().build();
    WebApplicationException exception = new WebApplicationException(response);
    assertEquals(response, anyExceptionMapper.toResponse(exception));
  }

  @Test
  public void toResponse_should_return_the_orignal_json_for_a_nested_WebApplicationException() {
    Response response = Response.ok().build();
    Exception exception = new Exception("test", new WebApplicationException(response));
    assertEquals(response, anyExceptionMapper.toResponse(exception));
  }

  @Test
  public void toResponse_should_return_the_exceptions_message_on_any_other_exception() {
    final String message = "this is a test";
    Exception exception = new Exception(message);
    Response response = anyExceptionMapper.toResponse(exception);
    assertTrue("Must be a Map", response.getEntity() instanceof Map<?, ?>);
    assertEquals("Must be a internal server error", 500, response.getStatus());
    assertEquals(message, getMessageFromResponse("message", response));
  }

  @SuppressWarnings("unchecked")
  private String getMessageFromResponse(String field, Response response) {
    return ((Map<String, String>) response.getEntity()).get(field);
  }
}