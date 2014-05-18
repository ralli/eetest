package de.fisp.eetest.test.unit.rest;

import de.fisp.eetest.test.integration.dao.PersonDao;
import de.fisp.eetest.dto.person.CreatePersonRequest;
import de.fisp.eetest.dto.person.CreatePersonResponse;
import de.fisp.eetest.dto.person.FindPersonsResponse;
import de.fisp.eetest.entities.Person;
import de.fisp.eetest.exceptions.NotFoundException;
import de.fisp.eetest.test.unit.service.PersonService;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

@Stateless
@Path("/persons")
@Produces("application/json")
public class PersonWebService
{
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
    if(person == null) {
      throw new WebApplicationException(createNotFoundResponse("Person nicht gefunden"));
    }
    return person;
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @TransactionAttribute
  public Response create(CreatePersonRequest createPersonRequest) {
    try {
      long personId = personService.create(createPersonRequest);
      CreatePersonResponse response = new CreatePersonResponse(personId);
      return Response.ok(response).build();
    } catch (ConstraintViolationException ce) {
      /**
       * Validierung über Standardvalidierung
       */
      return createViolationResponse(ce.getConstraintViolations());
    } catch (ValidationException e) {
      /**
       * Validierung über individuelle Validierung
       */
      return createValidationResponse(e.getMessage());
    } catch (Exception e) {
      /**
       * Alle anderen Exceptions (bspw. Datenbank nicht da)
       */
      return createExceptionResponse(e);
    }
  }

  @POST
  @Path("/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @TransactionAttribute
  public Response update(@PathParam("id") long id, CreatePersonRequest createPersonRequest) {
    try {
      personService.update(id, createPersonRequest);
      return Response.ok().build();
    }
    catch (NotFoundException ex) {
      return createNotFoundResponse(ex.getMessage());
    }
    catch (ConstraintViolationException ce) {
      /**
       * Validierung über Standardvalidierung
       */
      return createViolationResponse(ce.getConstraintViolations());
    } catch (ValidationException e) {
      /**
       * Validierung über individuelle Validierung
       */
      return createValidationResponse(e.getMessage());
    } catch (Exception e) {
      /**
       * Alle anderen Exceptions (bspw. Datenbank nicht da)
       */
      return createExceptionResponse(e);
    }
  }

  @POST
  @Path("/{id}/delete")
  @TransactionAttribute
  public Response delete(@PathParam("id") long id) {
    int count = personDao.deleteById(id);
    if(count == 0L) {
      return createNotFoundResponse("Person nicht gefunden");
    }
    else
      return Response.ok().build();
  }

  private Response createViolationResponse(Set<ConstraintViolation<?>> violations) {
    logger.info("Validation completed. violations found: " + violations.size());
    Map<String, String> responseObj = new HashMap<String, String>();
    for (ConstraintViolation<?> violation : violations) {
      responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
    }
    return Response.status(Response.Status.BAD_REQUEST).entity(responseObj).build();
  }

  private Response createNotFoundResponse(String message) {
    return Response.status(Response.Status.NOT_FOUND).entity(createMessage(message)).build();
  }

  private Response createValidationResponse(String message) {
    return Response.status(Response.Status.BAD_REQUEST).entity(createMessage(message)).build();
  }

  private Response createExceptionResponse(Exception e) {
    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(createMessage(e.getMessage())).build();
  }

  private Map<String, String> createMessage(String message) {
    Map<String, String> result = new HashMap<>();
    result.put("message", message);
    return result;
  }
}