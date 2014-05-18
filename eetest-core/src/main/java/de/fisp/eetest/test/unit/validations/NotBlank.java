package de.fisp.eetest.test.unit.validations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotBlankValidator.class)
@Documented
public @interface NotBlank {
  String message() default "Darf nicht leer sein.";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
