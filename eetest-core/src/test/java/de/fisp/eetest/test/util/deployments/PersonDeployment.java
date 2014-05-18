package de.fisp.eetest.test.util.deployments;

import de.fisp.eetest.test.integration.dao.PersonDao;
import de.fisp.eetest.dto.person.CreatePersonRequest;
import de.fisp.eetest.entities.Person;
import de.fisp.eetest.exceptions.NotFoundException;
import de.fisp.eetest.test.unit.validations.NotBlankValidator;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

import java.util.ArrayList;
import java.util.List;

public class PersonDeployment {
  public static Archive<?> createJarArchive(Class<?>... classes) {
    return ShrinkWrap.create(JavaArchive.class)
            .addPackages(false, getCorePackages(classes))
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
            .addAsResource("META-INF/persistence.xml");
  }

  private static Package[] getCorePackages(Class<?>... classes) {
    List<Package> packages = new ArrayList<>();
    if (classes != null) {
      for (Class<?> c : classes)
        packages.add(c.getPackage());
    }
    packages.add(PersonDao.class.getPackage());
    packages.add(CreatePersonRequest.class.getPackage());
    packages.add(Person.class.getPackage());
    packages.add(NotFoundException.class.getPackage());
    packages.add(NotBlankValidator.class.getPackage());
    return packages.toArray(new Package[]{});
  }
}
