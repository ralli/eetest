package de.fisp.eetest.test.unit.rest;

import de.fisp.eetest.dao.PersonDao;
import de.fisp.eetest.dto.person.CreatePersonRequest;
import de.fisp.eetest.dto.person.CreatePersonResponse;
import de.fisp.eetest.dto.person.FindPersonsResponse;
import de.fisp.eetest.entities.Person;
import de.fisp.eetest.exceptions.BusinessConstraintViolationException;
import de.fisp.eetest.exceptions.BusinessValidationException;
import de.fisp.eetest.exceptions.NotFoundException;
import de.fisp.eetest.rest.PersonWebService;
import de.fisp.eetest.service.PersonService;
import de.fisp.eetest.test.util.TestRuntimeDelegate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.RuntimeDelegate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static de.fisp.eetest.test.util.TestHelper.setAttribute;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    Assert.assertNotNull(result);
    Assert.assertTrue("Soll leer sein", result.getPersons().isEmpty());
  }

  @Test
  public void findById_should_throw_a_NotFoundException_if_person_not_found() {
    when(personDao.findById(anyLong())).thenReturn(null);
    try {
      personWebService.findById(10L);
      Assert.fail("Should throw a NotFoundException");
    }
    catch(NotFoundException ex) {
      // Ok
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
    when(personService.create((CreatePersonRequest) Matchers.any())).thenReturn(id);
    CreatePersonRequest request = new CreatePersonRequest();
    Response response = personWebService.create(request);
    assertEquals(OK, response.getStatus());
    Assert.assertTrue("Must be a CreatePersonResponse", response.getEntity() instanceof CreatePersonResponse);
    assertEquals("Must contain the Persons ID", id, ((CreatePersonResponse) response.getEntity()).getId());
  }

  @Test
  public void create_when_invalid_should_return_a_BAD_REQUEST() {
    String message = "Test validation failed";
    when(personService.create((CreatePersonRequest) Matchers.any())).thenThrow(new BusinessValidationException(message));
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

    when(personService.create((CreatePersonRequest) Matchers.any()))
            .thenThrow(new BusinessConstraintViolationException(message, violations));

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
    Mockito.doThrow(new BusinessValidationException(message))
            .when(personService)
            .update(anyLong(), (CreatePersonRequest) Matchers.any());
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

    Mockito.doThrow(new BusinessConstraintViolationException(message, violations))
            .when(personService)
            .update(anyLong(), (CreatePersonRequest) Matchers.any());

    CreatePersonRequest request = new CreatePersonRequest();
    Response response = personWebService.update(0L, request);

    assertBadRequestResponse(message, field, response);
  }

  private void assertBadRequestResponse(String message, String field, Response response) {
    assertEquals(BAD_REQUEST, response.getStatus());
    Assert.assertTrue("Must be a Map", response.getEntity() instanceof Map<?, ?>);
    assertEquals("Contain the validation message for the field",
            message,
            getMessageFromResponse(field, response));
  }

  @Test(expected = NotFoundException.class)
  public void delete_should_throw_a_NotFoundException_if_person_not_found() {
    personWebService.delete(0L);
  }

  @Test
  public void delete_should_return_OK_if_person_found() {
    when(personDao.deleteById(anyLong())).thenReturn(1);
    personWebService.delete(0L);
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

  @SuppressWarnings("unchecked")
  private String getMessageFromResponse(String field, Response response) {
    return ((Map<String, String>) response.getEntity()).get(field);
  }
}