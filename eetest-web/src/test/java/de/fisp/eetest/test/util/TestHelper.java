package de.fisp.eetest.test.util;

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
