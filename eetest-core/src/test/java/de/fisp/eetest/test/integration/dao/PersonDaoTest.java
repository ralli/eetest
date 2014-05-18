package de.fisp.eetest.test.integration.dao;

import de.fisp.eetest.dao.PersonDao;
import de.fisp.eetest.entities.Person;
import de.fisp.eetest.test.util.deployments.PersonDeployment;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(Arquillian.class)
public class PersonDaoTest {
  @Deployment
  public static Archive<?> deployment() {
    return PersonDeployment.createJarArchive();
  }

  @Inject
  private PersonDao personDao;

  @Inject
  private UserTransaction utx;

  @PersistenceContext
  private EntityManager em;

  @Test
  public void findAll_should_return_a_list_of_persons() {
    List<Person> list = personDao.findAll();
    assertNotNull(list);
  }

  @Test
  public void findById_should_return_null_if_not_found() {
    Person person = personDao.findById(0L);
    assertNull(person);
  }

  @Test
  public void findById_should_return_a_person_if_found() {
    startTransaction();
    try {
      Person person = insertPerson();
      person = personDao.findById(person.getId());
      assertNotNull(person);
    }
    finally {
      rollbackTransaction();
    }
  }

  @Test
  public void update_should_change_the_persons_attributes() {
    startTransaction();
    try {
      Person person = insertPerson();
      final String vorname = "Neu";
      final String nachname = "Auch neu";
      person.setVorname(vorname);
      person.setNachname(nachname);
      personDao.update(person);
      person = personDao.findById(person.getId());
      assertEquals(vorname, person.getVorname());
      assertEquals(nachname, person.getNachname());
    }
    finally {
      rollbackTransaction();
    }
  }

  @Test
  public void delete_should_delete_the_person() {
    startTransaction();
    try {
      Person person = insertPerson();
      long id = person.getId();
      personDao.deleteById(id);
      person = personDao.findById(id);
      assertNull(person);
    }
    finally {
      rollbackTransaction();
    }
  }

  private Person insertPerson() {
    Person person = new Person();
    person.setVorname("Testi");
    person.setNachname("Test");
    personDao.insert(person);
    return person;
  }

  private void startTransaction() {
    try {
      utx.begin();
      em.joinTransaction();
    }
    catch(Exception ex) {
      throw new RuntimeException("startTransaction", ex);
    }
  }

  private void rollbackTransaction() {
    try {
      utx.rollback();
    }
    catch(Exception ex) {
      throw new RuntimeException("rollbackTransaction", ex);
    }
  }
}