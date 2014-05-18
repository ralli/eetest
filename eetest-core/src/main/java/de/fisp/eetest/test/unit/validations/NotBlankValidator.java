package de.fisp.eetest.test.unit.validations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotBlankValidator implements ConstraintValidator<NotBlank, String> {
  @Override
  public void initialize(NotBlank constraintAnnotation) {
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return value != null && !value.trim().isEmpty();
  }
}
