package de.fisp.eetest.dao;

import de.fisp.eetest.entities.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.TransactionAttribute;
import javax.enterprise.inject.Default;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Named
@Default
public class PersonDao {
  @PersistenceContext
  private EntityManager em;

  private static final Logger log = LoggerFactory.getLogger(PersonDao.class);

  /**
   * Liefert eine Liste aller Personen
   */
  public List<Person> findAll() {
    String ql = "select p from Person p";
    List<Person> result = em.createQuery(ql, Person.class).getResultList();
    log.info("findAll(): {} persons found", result.size());
    return result;
  }

  /**
   * Liefert eine Person anhand des Primärschlüssels
   *
   * @param id der Primärschlüssel der Person
   * @return Die Person oder <code>null</code>, wenn die Person nicht gefunden wurde
   */
  public Person findById(long id) {
    String ql = "select p from Person p where p.id=:id";
    TypedQuery<Person> q = em.createQuery(ql, Person.class);
    q.setParameter("id", id);
    List<Person> list = q.getResultList();
    Person result = list.isEmpty() ? null : list.get(0);
    log.info("findById({}) = {}", id, result);
    return result;
  }

  /**
   * Speichert eine Person
   */
  @TransactionAttribute
  public void insert(Person person) {
    log.info("insert({})", person);
    em.persist(person);
  }

  /**
   * Speichert die Änderungen an einer bestehenden Person
   */
  @TransactionAttribute
  public void update(Person person) {
    log.info("update({})", person);
    em.merge(person);
  }

  @TransactionAttribute
  public int deleteById(long id) {
    Person p = findById(id);
    if(p == null)
      return 0;
    em.remove(p);
    return 1;
  }


}
