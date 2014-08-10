package de.fisp.eetest.test.util;

import org.apache.deltaspike.core.api.exclude.Exclude;
import org.apache.deltaspike.core.api.projectstage.ProjectStage;
import org.apache.deltaspike.jpa.api.transaction.TransactionScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

@Exclude(exceptIfProjectStage = ProjectStage.UnitTest.class)
public class TestResources {
  public static final Logger log = LoggerFactory.getLogger(TestResources.class);

  @Produces
  public Logger produceLog(InjectionPoint injectionPoint) {
    return LoggerFactory.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
  }

  @Produces
  @ApplicationScoped
  public EntityManagerFactory createEntityManagerFactory() {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("hase");
    log.info("created entityManagerfactory: {}", emf);
    return emf;
  }

  @ApplicationScoped
  @Produces
  public ValidatorFactory createValidatorFactory() {
    log.info("creating validator factory...");
    return Validation.buildDefaultValidatorFactory();
  }

  @Produces
  public Validator createValidator(ValidatorFactory validatorFactory) {
    log.info("creating validator...");
    return validatorFactory.getValidator();
  }

//  public void destroyEMF(@Disposes EntityManagerFactory factory) {
//    log.info("Destroying Entitymanagerfactory");
//    factory.close();
//  }

  @Produces
  public EntityManager createEntityManager(EntityManagerFactory entityManagerFactory) {
    EntityManager em = entityManagerFactory.createEntityManager();
    log.info("entity manager factory = {}", entityManagerFactory);
    log.info("creating entitymanager: {}", em);
    return em;
  }

//  public void destroyEM(@Disposes EntityManager em) {
//    log.info("Destroying Entitymanager");
//    em.close();
//  }
}
