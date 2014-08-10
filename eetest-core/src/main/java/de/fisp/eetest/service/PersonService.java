package de.fisp.eetest.service;

import de.fisp.eetest.dao.PersonDao;
import de.fisp.eetest.dto.person.CreatePersonRequest;
import de.fisp.eetest.entities.Person;
import de.fisp.eetest.exceptions.BusinessConstraintViolationException;
import de.fisp.eetest.exceptions.BusinessValidationException;
import de.fisp.eetest.exceptions.NotFoundException;
import org.apache.deltaspike.jpa.api.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.HashSet;
import java.util.Set;


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
   * @throws BusinessValidationException wenn die Person nicht valide ist.
   */
  @Transactional
  public long create(CreatePersonRequest createPersonRequest) throws BusinessValidationException {
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
   * @throws BusinessValidationException wenn die Persion nicht valide ist.
   */
  @Transactional
  public void update(long id, CreatePersonRequest createPersonRequest) throws BusinessValidationException, NotFoundException {
    log.info("update({}, {})", id, createPersonRequest);
    validatePerson(createPersonRequest);
    Person person = personDao.findById(id);

    if (person == null) {
      throw new NotFoundException("Person nicht gefunden");
    }
    person.setVorname(createPersonRequest.getVorname());
    person.setNachname(createPersonRequest.getNachname());
    personDao.update(person);
  }

  private void validatePerson(CreatePersonRequest request) throws BusinessValidationException {
    Set<ConstraintViolation<CreatePersonRequest>> violations = validator.validate(request);
    if (!violations.isEmpty()) {
      throw new BusinessConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
    }

    /**
     * So kann man kompliziertere Validierungen machen
     * (Leute sind zu Jung, Verheiratet, Tot, Unique-Constraints etc.)
     */
    if (request.getNachname() != null && request.getNachname().startsWith("Hase"))
      throw new BusinessValidationException("Hasen sind nicht erlaubt");
  }

  private Person createPersonFromRequest(CreatePersonRequest createPersonRequest) {
    Person person = new Person();
    person.setVorname(createPersonRequest.getVorname());
    person.setNachname(createPersonRequest.getNachname());
    return person;
  }
}
