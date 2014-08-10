package de.fisp.eetest.dao;

import de.fisp.eetest.entities.Person;
import org.apache.deltaspike.jpa.api.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;


public class PersonDao {
  @Inject
  private EntityManager entityManager;

  private static final Logger log = LoggerFactory.getLogger(PersonDao.class);

  /**
   * Liefert eine Liste aller Personen
   */

  @Transactional
  public List<Person> findAll() {
    String ql = "select p from Person p";
    List<Person> result = entityManager.createQuery(ql, Person.class).getResultList();
    log.info("findAll(): {} persons found", result.size());
    return result;
  }

  /**
   * Liefert eine Person anhand des Primärschlüssels
   *
   * @param id der Primärschlüssel der Person
   * @return Die Person oder <code>null</code>, wenn die Person nicht gefunden wurde
   */
  @Transactional
  public Person findById(long id) {
    String ql = "select p from Person p where p.id=:id";
    TypedQuery<Person> q = entityManager.createQuery(ql, Person.class);
    q.setParameter("id", id);
    List<Person> list = q.getResultList();
    Person result = list.isEmpty() ? null : list.get(0);
    log.info("findById({}) = {}", id, result);
    return result;
  }

  /**
   * Speichert eine Person
   */
  @Transactional
  public void insert(Person person) {
    log.info("insert({})", person);
    entityManager.persist(person);
  }

  /**
   * Speichert die Änderungen an einer bestehenden Person
   */
  @Transactional
  public void update(Person person) {
    log.info("update({})", person);
    entityManager.merge(person);
  }

  /**
   * Löscht eine Person.
   * @param id Der Primärschlüssel der Person
   * @return 1, wenn die Person nicht gelöscht wurde, 0 sonst.
   */
  @Transactional
  public int deleteById(long id) {
    Person p = findById(id);
    if(p == null)
      return 0;
    entityManager.remove(p);
    return 1;
  }
}
