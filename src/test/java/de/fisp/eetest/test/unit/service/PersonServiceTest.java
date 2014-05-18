package de.fisp.eetest.test.unit.service;

import de.fisp.eetest.test.integration.dao.PersonDao;
import de.fisp.eetest.dto.person.CreatePersonRequest;
import de.fisp.eetest.entities.Person;
import de.fisp.eetest.exceptions.NotFoundException;
import org.junit.Before;
import org.junit.Test;

import javax.validation.*;
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

  @Test
  public void create_when_invalid_should_throw_a_ConstraintValidationException() {
    Set<ConstraintViolation<?>> constraintViolations = new HashSet<>();
    constraintViolations.add(createConstraintViolation("firstName", "Test validation message"));
    when(validator.validate(any(Person.class))).thenThrow(new ConstraintViolationException(constraintViolations));
    try {
      personService.create(new CreatePersonRequest());
      fail("should throw a ConstraintValidationException");
    } catch (ConstraintViolationException ex) {
      // alles ok
    }
  }

  @Test
  public void create_when_nachname_starts_with_Hase_should_throw_a_ValidationExeption() {
    try {
      CreatePersonRequest request = new CreatePersonRequest();
      request.setNachname("Hasexxx");
      personService.create(request);
      fail("should throw a ValidationException");
    } catch (ValidationException ex) {
      // alles ok
    }
  }

  @Test
  public void update_successful() {
    Person person = new Person();
    long id = 10L;
    when(personDao.findById(id)).thenReturn(person);
    CreatePersonRequest request = new CreatePersonRequest();
    personService.update(id, request);
  }

  @Test
  public void update_when_person_not_found_should_throw_a_NotFoundException() {
    try {
      long id = 10L;
      when(personDao.findById(id)).thenReturn(null);
      CreatePersonRequest request = new CreatePersonRequest();
      personService.update(id, request);
      fail("should throw a NotFoundException");
    }
    catch(NotFoundException ex) {
      // alles ok
    }
  }

  @Test
  public void update_when_validation_fails_should_throw_a_ConstraintViolationException() {
    Set<ConstraintViolation<?>> constraintViolations = new HashSet<>();
    constraintViolations.add(createConstraintViolation("firstName", "Test validation message"));
    when(validator.validate(any(Person.class))).thenThrow(new ConstraintViolationException(constraintViolations));
    try {
      personService.update(0L, new CreatePersonRequest());
      fail("should throw a ConstraintValidationException");
    } catch (ConstraintViolationException ex) {
      // alles ok
    }
  }

  @Test
  public void update_when_nachname_starts_with_Hase_should_throw_a_ValidationExeption() {
    try {
      CreatePersonRequest request = new CreatePersonRequest();
      request.setNachname("Hasexxx");
      personService.update(0L, request);
      fail("should throw a ValidationException");
    } catch (ValidationException ex) {
      // alles ok
    }
  }


  private <T> ConstraintViolation<T> createConstraintViolation(String field, String message) {
    ConstraintViolation<T> violation = mock(ConstraintViolation.class);
    Path path = mock(Path.class);
    when(path.toString()).thenReturn(field);
    when(violation.getPropertyPath()).thenReturn(path);
    when(violation.getMessage()).thenReturn(message);
    return violation;
  }
}