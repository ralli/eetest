package de.fisp.eetest.test.util;

import javax.enterprise.inject.Produces;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.lang.reflect.Field;

public class TestHelper {
  public static void setAttribute(Object obj, String fieldName, Object value) {
    try {
      Class<?> c = obj.getClass();
      Field field = c.getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(obj, value);
    } catch (Exception ex) {
      throw new RuntimeException("Fehler in SetAttribute", ex);
    }
  }
}
