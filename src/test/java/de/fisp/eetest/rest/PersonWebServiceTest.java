package de.fisp.eetest.rest;

import de.fisp.eetest.dao.PersonDao;
import de.fisp.eetest.dto.person.CreatePersonRequest;
import de.fisp.eetest.dto.person.CreatePersonResponse;
import de.fisp.eetest.dto.person.FindPersonsResponse;
import de.fisp.eetest.entities.Person;
import de.fisp.eetest.service.PersonService;
import de.fisp.eetest.test.util.TestRuntimeDelegate;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import javax.validation.ValidationException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.RuntimeDelegate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static de.fisp.eetest.test.util.TestHelper.setAttribute;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PersonWebServiceTest {
  private static final Logger logger = LoggerFactory.getLogger(PersonWebService.class);
  public static final int BAD_REQUEST = 400;
  public static final int OK = 200;
  public static final int NOT_FOUND = 404;
  private PersonDao personDao;
  private PersonService personService;
  private PersonWebService personWebService;

  @Before
  public void setUp() throws Exception {
    personDao = mock(PersonDao.class);
    personService = mock(PersonService.class);
    personWebService = new PersonWebService();
    setAttribute(personWebService, "personDao", personDao);
    setAttribute(personWebService, "personService", personService);
    setAttribute(personWebService, "logger", logger);
    RuntimeDelegate.setInstance(new TestRuntimeDelegate());
  }

  @Test
  public void findAll_should_return_FindPersonsResponse() {
    when(personDao.findAll()).thenReturn(new ArrayList<Person>());
    FindPersonsResponse result = personWebService.findAll();
    assertNotNull(result);
    assertTrue("Soll leer sein", result.getPersons().isEmpty());
  }

  @Test
  public void findById_should_throw_a_WebApplicationException_if_person_not_found() {
    when(personDao.findById(anyLong())).thenReturn(null);
    try {
      personWebService.findById(10L);
      fail("Should throw a NotFoundException");
    }
    catch(WebApplicationException ex) {
      assertEquals(NOT_FOUND, ex.getResponse().getStatus());
    }
  }

  @Test
  public void findById_should_return_a_person() {
    Person person = new Person();
    when(personDao.findById(anyLong())).thenReturn(person);
    assertEquals(person, personWebService.findById(0l));
  }

  @Test
  public void create_when_successful_should_return_OK() {
    final long id = 10L;
    when(personService.create((CreatePersonRequest) any())).thenReturn(id);
    CreatePersonRequest request = new CreatePersonRequest();
    Response response = personWebService.create(request);
    assertEquals(OK, response.getStatus());
    assertTrue("Must be a CreatePersonResponse", response.getEntity() instanceof CreatePersonResponse);
    assertEquals("Must contain the Persons ID", id, ((CreatePersonResponse) response.getEntity()).getId());
  }

  @Test
  public void create_when_invalid_should_return_a_BAD_REQUEST() {
    String message = "Test validation failed";
    when(personService.create((CreatePersonRequest) any())).thenThrow(new ValidationException(message));
    CreatePersonRequest request = new CreatePersonRequest();
    Response response = personWebService.create(request);

    assertBadRequestResponse(message, "message", response);
  }

  @Test
  public void create_when_validation_errors_occur_should_return_a_BAD_REQUEST() {
    final String message = "Test validation failed";
    final String field = "firstName";

    Set<ConstraintViolation<?>> violations = new HashSet<>();
    ConstraintViolation<Person> violation = createConstraintViolation(field, message);
    violations.add(violation);

    when(personService.create((CreatePersonRequest) any()))
            .thenThrow(new ConstraintViolationException(message, violations));

    CreatePersonRequest request = new CreatePersonRequest();
    Response response = personWebService.create(request);

    assertBadRequestResponse(message, field, response);
  }

  @Test
  public void update_when_successful_should_return_OK() {
    CreatePersonRequest request = new CreatePersonRequest();
    Response response = personWebService.update(0L, request);
    assertEquals(OK, response.getStatus());
  }

  @Test
  public void update_when_invalid_should_return_a_BAD_REQUEST() {
    String message = "Test validation failed";
    doThrow(new ValidationException(message))
            .when(personService)
            .update(anyLong(), (CreatePersonRequest) any());
    CreatePersonRequest request = new CreatePersonRequest();
    Response response = personWebService.update(0L, request);

    assertBadRequestResponse(message, "message", response);
  }


  @Test
  public void update_when_validation_errors_occur_should_return_a_BAD_REQUEST() {
    final String message = "Test validation failed";
    final String field = "firstName";

    Set<ConstraintViolation<?>> violations = new HashSet<>();
    ConstraintViolation<Person> violation = createConstraintViolation(field, message);
    violations.add(violation);

    doThrow(new ConstraintViolationException(message, violations))
            .when(personService)
            .update(anyLong(), (CreatePersonRequest) any());

    CreatePersonRequest request = new CreatePersonRequest();
    Response response = personWebService.update(0L, request);

    assertBadRequestResponse(message, field, response);
  }

  private void assertBadRequestResponse(String message, String field, Response response) {
    assertEquals(BAD_REQUEST, response.getStatus());
    assertTrue("Must be a Map", response.getEntity() instanceof Map<?, ?>);
    assertEquals("Contain the validation message for the field",
            message,
            getMessageFromResponse(field, response));
  }

  @Test
  public void delete_should_return_NOT_FOUND_if_person_not_found() {
    Response response = personWebService.delete(0L);
    assertEquals(NOT_FOUND, response.getStatus());
  }

  @Test
  public void delete_should_return_OK_if_person_found() {
    when(personDao.deleteById(anyLong())).thenReturn(1);
    Response response = personWebService.delete(0L);
    assertEquals(OK, response.getStatus());
  }


  private <T> ConstraintViolation<T> createConstraintViolation(String field, String message) {
    ConstraintViolation<T> violation = mock(ConstraintViolation.class);
    Path path = mock(Path.class);
    when(path.toString()).thenReturn(field);
    when(violation.getPropertyPath()).thenReturn(path);
    when(violation.getMessage()).thenReturn(message);
    return violation;
  }

  @SuppressWarnings("unchecked")
  private String getMessageFromResponse(String field, Response response) {
    return ((Map<String, String>) response.getEntity()).get(field);
  }
}