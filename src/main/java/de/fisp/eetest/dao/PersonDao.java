package de.fisp.eetest.dao;

import de.fisp.eetest.entities.Person;

import javax.annotation.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@ManagedBean
public class PersonDao {
  @PersistenceContext
  EntityManager em;

  public List<Person> findAll() {
    return em.createQuery("select p from Person p").getResultList();
  }
}
