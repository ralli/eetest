package de.fisp.eetest.rest;

import de.fisp.eetest.dao.PersonDao;
import de.fisp.eetest.entities.Person;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static de.fisp.eetest.test.util.TestHelper.setAttribute;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class PersonWebServiceTest {

  private PersonDao personDao;
  private static final Logger logger = LoggerFactory.getLogger(PersonWebService.class);
  private PersonWebService personWebService;


  @Before
  public void setUp() throws Exception {
    personDao = Mockito.mock(PersonDao.class);
    personWebService = new PersonWebService();
    setAttribute(personWebService, "personDao", personDao);
    setAttribute(personWebService, "logger", logger);
  }

  @Test
  public void find_all_should_return_a_list() {
    Mockito.when(personDao.findAll()).thenReturn(new ArrayList<Person>());
    List<Person> result = personWebService.findAll();
    assertNotNull(result);
    assertTrue("Soll leer sein", result.isEmpty());
  }
}