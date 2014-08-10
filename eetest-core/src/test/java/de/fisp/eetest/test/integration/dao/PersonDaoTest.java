package de.fisp.eetest.test.integration.dao;

import de.fisp.eetest.dao.PersonDao;
import de.fisp.eetest.entities.Person;
import org.apache.deltaspike.core.api.projectstage.ProjectStage;
import org.apache.deltaspike.jpa.api.transaction.Transactional;
import org.apache.deltaspike.testcontrol.api.TestControl;
import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;

import javax.inject.Inject;
import java.util.List;

import static org.junit.Assert.assertNotNull;

@RunWith(CdiTestRunner.class)
@TestControl(projectStage = ProjectStage.UnitTest.class, logHandler = SLF4JBridgeHandler.class)
public class PersonDaoTest {

  @Inject
  private PersonDao personDao;

  @Inject
  private ProjectStage projectStage;


  @Test
  public void personDao_should_not_be_null() {
    assertNotNull(personDao);
  }

  @Test
  public void findAll_should_return_a_list_of_persons() {
    List<Person> list = personDao.findAll();
    assertNotNull(list);
  }
//
//  @Test
//  public void findById_should_return_null_if_not_found() {
//    Person person = personDao.findById(0L);
//    assertNull(person);
//  }
//
//  @Test
//  public void findById_should_return_a_person_if_found() {
//    Person person = insertPerson();
//    person = personDao.findById(person.getId());
//    assertNotNull(person);
//  }
//
//  @Test
//  public void update_should_change_the_persons_attributes() {
//    Person person = insertPerson();
//    final String vorname = "Neu";
//    final String nachname = "Auch neu";
//    person.setVorname(vorname);
//    person.setNachname(nachname);
//    personDao.update(person);
//    person = personDao.findById(person.getId());
//    assertEquals(vorname, person.getVorname());
//    assertEquals(nachname, person.getNachname());
//  }
//
//  @Test
//  public void delete_should_delete_the_person() {
//    Person person = insertPerson();
//    long id = person.getId();
//    personDao.deleteById(id);
//    person = personDao.findById(id);
//    assertNull(person);
//  }
//
//  private Person insertPerson() {
//    Person person = new Person();
//    person.setVorname("Testi");
//    person.setNachname("Test");
//    personDao.insert(person);
//    return person;
//  }
}