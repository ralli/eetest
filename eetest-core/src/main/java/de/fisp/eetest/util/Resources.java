package de.fisp.eetest.util;

import org.apache.deltaspike.core.api.exclude.Exclude;
import org.apache.deltaspike.core.api.projectstage.ProjectStage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Exclude(exceptIfProjectStage = ProjectStage.Production.class)
public class Resources {
  @PersistenceContext(name="forge-default", unitName = "forge-default")
  private EntityManager entityManager;
  public static final Logger log = LoggerFactory.getLogger(Resources.class);


  @Produces
  public Logger produceLog(InjectionPoint injectionPoint) {
    return LoggerFactory.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
  }

  @Produces
  @RequestScoped
  @Default
  public EntityManager createEntityManager() {
    log.info("creating entity manager: {}", entityManager);
    return entityManager;
  }

//  public void dispose(@Disposes @Default EntityManager entityManager)
//  {
//    log.info("closing entityManager: {}", entityManager);
//    if (entityManager.isOpen())
//    {
//      entityManager.close();
//    }
//  }
}
