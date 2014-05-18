package de.fisp.eetest.test.unit.validations;

import de.fisp.eetest.validations.NotBlankValidator;
import org.junit.Test;

import static org.junit.Assert.*;

public class NotBlankValidatorTest {
  private NotBlankValidator notBlankValidator = new NotBlankValidator();

  @Test
  public void isValid_should_return_true_on_valid_values() {
    String value = "Valid";
    assertTrue("Is Valid", notBlankValidator.isValid(value, null));
  }

  @Test
  public void isValid_should_return_false_on_invalid_values() {
    assertFalse("Should reject null values", notBlankValidator.isValid(null, null));
    assertFalse("Should reject empty values", notBlankValidator.isValid("", null));
    assertFalse("Should reject values with only blanks (isSpace = true)", notBlankValidator.isValid(" \n\t\r",  null));
  }
}