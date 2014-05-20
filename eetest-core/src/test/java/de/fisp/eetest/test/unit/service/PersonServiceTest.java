package de.fisp.eetest.test.unit.service;

import de.fisp.eetest.dao.PersonDao;
import de.fisp.eetest.dto.person.CreatePersonRequest;
import de.fisp.eetest.entities.Person;
import de.fisp.eetest.exceptions.BusinessConstraintViolationException;
import de.fisp.eetest.exceptions.BusinessValidationException;
import de.fisp.eetest.exceptions.NotFoundException;
import de.fisp.eetest.service.PersonService;
import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.Validator;
import java.util.HashSet;
import java.util.Set;

import static de.fisp.eetest.test.util.TestHelper.setAttribute;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class PersonServiceTest {
  private Validator validator;
  private PersonDao personDao;
  private PersonService personService;

  @Before
  public void initialize() {
    validator = mock(Validator.class);
    personDao = mock(PersonDao.class);
    personService = new PersonService();
    setAttribute(personService, "validator", validator);
    setAttribute(personService, "personDao", personDao);
  }

  @Test
  public void create_when_valid_should_return_the_persons_id() {
    long id = personService.create(new CreatePersonRequest());
    assertEquals(0L, id);
  }

  @Test(expected = BusinessConstraintViolationException.class)
  public void create_when_invalid_should_throw_a_BusinessConstraintViolationException() {
    Set<ConstraintViolation<CreatePersonRequest>> constraintViolations = new HashSet<>();
    constraintViolations.add(createConstraintViolation("firstName", "Test validation message", CreatePersonRequest.class));
    when(validator.validate(any(CreatePersonRequest.class))).thenReturn(constraintViolations);
    personService.create(new CreatePersonRequest());
  }

  @Test(expected = BusinessValidationException.class)
  public void create_when_nachname_starts_with_Hase_should_throw_a_BusinessValidationException() {
    CreatePersonRequest request = new CreatePersonRequest();
    request.setNachname("Hasexxx");
    personService.create(request);
    fail("should throw a BusinessValidationException");
  }

  @Test
  public void update_successful() {
    Person person = new Person();
    long id = 10L;
    when(personDao.findById(id)).thenReturn(person);
    CreatePersonRequest request = new CreatePersonRequest();
    personService.update(id, request);
  }

  @Test(expected = NotFoundException.class)
  public void update_when_person_not_found_should_throw_a_NotFoundException() {
    long id = 10L;
    when(personDao.findById(id)).thenReturn(null);
    CreatePersonRequest request = new CreatePersonRequest();
    personService.update(id, request);
  }

  @Test(expected = BusinessConstraintViolationException.class)
  public void update_when_validation_fails_should_throw_a_BusinessConstraintViolationException() {
    Set<ConstraintViolation<CreatePersonRequest>> constraintViolations = new HashSet<>();
    constraintViolations.add(createConstraintViolation("firstName", "Test validation message", CreatePersonRequest.class));
    when(validator.validate(any(CreatePersonRequest.class))).thenReturn(constraintViolations);
    personService.update(0L, new CreatePersonRequest());
  }

  @Test(expected = BusinessValidationException.class)
  public void update_when_nachname_starts_with_Hase_should_throw_a_BusinessValidationException() {
    CreatePersonRequest request = new CreatePersonRequest();
    request.setNachname("Hasexxx");
    personService.update(0L, request);
  }


  private <T> ConstraintViolation<T> createConstraintViolation(String field, String message, Class<T> clazz) {
    @SuppressWarnings("unchecked")
    ConstraintViolation<T> violation = mock(ConstraintViolation.class);
    Path path = mock(Path.class);
    when(path.toString()).thenReturn(field);
    when(violation.getPropertyPath()).thenReturn(path);
    when(violation.getMessage()).thenReturn(message);
    return violation;
  }
}