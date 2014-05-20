package de.fisp.eetest.rest;

import de.fisp.eetest.dao.PersonDao;
import de.fisp.eetest.dto.person.CreatePersonRequest;
import de.fisp.eetest.dto.person.CreatePersonResponse;
import de.fisp.eetest.dto.person.FindPersonsResponse;
import de.fisp.eetest.entities.Person;
import de.fisp.eetest.exceptions.NotFoundException;
import de.fisp.eetest.service.PersonService;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/persons")
@Produces("application/json")
public class PersonWebService {
  @Inject
  private PersonDao personDao;

  @Inject
  private PersonService personService;

  @Inject
  private Logger logger;

  @Inject
  private Validator validator;

  @PersistenceContext
  private EntityManager em;

  @GET
  public FindPersonsResponse findAll() {
    List<Person> result = personDao.findAll();
    logger.info("findAll(): {} found.", result.size());
    return new FindPersonsResponse(result);
  }

  @GET
  @Path("/{id}")
  public Person findById(@PathParam("id") long id) {
    Person person = personDao.findById(id);
    if (person == null) {
      throw new NotFoundException("Person nicht gefunden");
    }
    return person;
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public CreatePersonResponse create(CreatePersonRequest createPersonRequest) {
    long personId = personService.create(createPersonRequest);
    CreatePersonResponse response = new CreatePersonResponse(personId);
    return response;
  }

  @POST
  @Path("/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  public void update(@PathParam("id") long id, CreatePersonRequest createPersonRequest) {
    personService.update(id, createPersonRequest);
  }

  @POST
  @Path("/{id}/delete")
  public void delete(@PathParam("id") long id) {
    int count = personDao.deleteById(id);
    if (count == 0) {
      throw new NotFoundException("Person nicht gefunden");
    }
  }
}