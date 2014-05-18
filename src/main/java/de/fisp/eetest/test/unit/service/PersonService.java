package de.fisp.eetest.test.unit.service;

import de.fisp.eetest.test.integration.dao.PersonDao;
import de.fisp.eetest.dto.person.CreatePersonRequest;
import de.fisp.eetest.entities.Person;
import de.fisp.eetest.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.TransactionAttribute;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.util.HashSet;
import java.util.Set;

@Named
public class PersonService {
  @Inject
  private PersonDao personDao;

  @Inject
  private Validator validator;

  private static final Logger log = LoggerFactory.getLogger(PersonService.class);

  /**
   * Speichert eine Person.
   *
   * @param createPersonRequest Die für die Personenanlage notwendigen Parmeter
   * @return Die ID der angelegten Person
   * @throws ValidationException wenn die Person nicht valide ist.
   */
  @TransactionAttribute
  public long create(CreatePersonRequest createPersonRequest) throws ValidationException {
    log.info("create({})", createPersonRequest);
    validatePerson(createPersonRequest);
    Person person = createPersonFromRequest(createPersonRequest);
    personDao.insert(person);
    return person.getId();
  }

  /**
   * Speichert eine Person.
   *
   * @param createPersonRequest Die für die Personenanlage notwendigen Parmeter
   * @return Die ID der angelegten Person
   * @throws ValidationException wenn die Persion nicht valide ist.
   */
  @TransactionAttribute
  public void update(long id, CreatePersonRequest createPersonRequest) throws ValidationException, NotFoundException {
    log.info("update({}, {})", id, createPersonRequest);
    validatePerson(createPersonRequest);
    Person person = personDao.findById(id);

    if(person == null) {
      throw new NotFoundException("Person nicht gefunden");
    }
    person.setVorname(createPersonRequest.getVorname());
    person.setNachname(createPersonRequest.getNachname());
    personDao.update(person);
  }

  private void validatePerson(CreatePersonRequest request) throws ValidationException {
    Set<ConstraintViolation<CreatePersonRequest>> violations = validator.validate(request);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
    }

    /**
     * So kann man kompliziertere Validierungen machen
     * (Leute sind zu Jung, Verheiratet, Tot, Unique-Constraints etc.)
     */
    if(request.getNachname() != null && request.getNachname().startsWith("Hase"))
      throw new javax.validation.ValidationException("Hasen sind nicht erlaubt");
  }

  private Person createPersonFromRequest(CreatePersonRequest createPersonRequest) {
    Person person = new Person();
    person.setVorname(createPersonRequest.getVorname());
    person.setNachname(createPersonRequest.getNachname());
    return person;
  }
}
