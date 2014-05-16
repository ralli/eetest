package de.fisp.eetest.rest;

import de.fisp.eetest.dao.PersonDao;
import de.fisp.eetest.entities.Person;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;

@Stateless
@Path("/persons")
@Produces("application/json")
public class PersonWebService
{
  @Inject
  private PersonDao personDao;

  @Inject
  private Logger logger;

  @GET
  public List<Person> findAll() {
    List<Person> result = personDao.findAll();
    logger.info("findAll(): {} found.", result.size());
    return result;
  }

}