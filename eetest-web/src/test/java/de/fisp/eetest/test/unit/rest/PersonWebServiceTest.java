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
import javax.ws.rs.ext.RuntimeDelegate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static de.fisp.eetest.test.util.TestHelper.setAttribute;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PersonWebServiceTest {
  private static final Logger logger = LoggerFactory.getLogger(PersonWebService.class);
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
    } catch (NotFoundException ex) {
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
  public void create_when_successful_should_return_a_CreatePersonResponse() {
    final long id = 10L;
    when(personService.create((CreatePersonRequest) Matchers.any())).thenReturn(id);
    CreatePersonRequest request = new CreatePersonRequest();
    CreatePersonResponse response = personWebService.create(request);
    assertEquals("Must contain the Persons ID", id, response.getId());
  }

  @Test(expected = BusinessValidationException.class)
  public void create_when_invalid_should_throw_a_BusinessValidationException() {
    String message = "Test validation failed";
    when(personService.create((CreatePersonRequest) Matchers.any())).thenThrow(new BusinessValidationException(message));
    CreatePersonRequest request = new CreatePersonRequest();
    personWebService.create(request);
  }

  @Test
  public void update_when_successful_should_not_throw_exceptions() {
    CreatePersonRequest request = new CreatePersonRequest();
    personWebService.update(0L, request);
  }

  @Test(expected = BusinessConstraintViolationException.class)
  public void update_when_validation_errors_occur_should_return_a_BusinessConstraintViolationException() {
    final String message = "Test validation failed";
    final String field = "firstName";

    Set<ConstraintViolation<?>> violations = new HashSet<>();
    ConstraintViolation<Person> violation = createConstraintViolation(field, message);
    violations.add(violation);

    Mockito.doThrow(new BusinessConstraintViolationException(message, violations))
            .when(personService)
            .update(anyLong(), (CreatePersonRequest) Matchers.any());

    CreatePersonRequest request = new CreatePersonRequest();
    personWebService.update(0L, request);
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
}