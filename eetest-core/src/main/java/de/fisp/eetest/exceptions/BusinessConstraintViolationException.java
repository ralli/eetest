package de.fisp.eetest.exceptions;

import de.fisp.eetest.exceptions.handler.BaseExceptionHandler;

import javax.validation.ConstraintViolation;
import java.util.Set;

/**
 * Exception, die Validierungsfehler weitergeleitet.
 *
 * Die Exception rollt die aktuelle EJB-Transaktion zurück.
 * Die Exception wird nicht automatisch in eine EJBException eingepackt.
 *
 * <code>
 *   @Inject
 *   Validator validator;
 *
 *   ....
 *
 *   Set<ConstraintViolation<BusinessObject>> violations = validator.validate(businessObject);
 *   if(!violations.isEmpty())
 *     throw new BusinessConstraintViolationException(violations)
 * </code>
 */
public class BusinessConstraintViolationException extends BusinessValidationException {
  private final Set<ConstraintViolation<?>> constraintViolations;

  public BusinessConstraintViolationException(Set<ConstraintViolation<?>> constraintViolations) {
    super();
    this.constraintViolations = constraintViolations;
  }

  public BusinessConstraintViolationException(String message, Set<ConstraintViolation<?>> constraintViolations) {
    super(message);
    this.constraintViolations = constraintViolations;
  }

  public BusinessConstraintViolationException(String message, Throwable cause, Set<ConstraintViolation<?>> constraintViolations) {
    super(message, cause);
    this.constraintViolations = constraintViolations;
  }

  public BusinessConstraintViolationException(Throwable cause, Set<ConstraintViolation<?>> constraintViolations) {
    super(cause);
    this.constraintViolations = constraintViolations;
  }

  public BusinessConstraintViolationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Set<ConstraintViolation<?>> constraintViolations) {
    super(message, cause, enableSuppression, writableStackTrace);
    this.constraintViolations = constraintViolations;
  }

  public <T> T handle(BaseExceptionHandler<T> handler) {
    return handler.handleBusinessConstraintViolationException(this).getResult();
  }

  public Set<ConstraintViolation<?>> getConstraintViolations() {
    return constraintViolations;
  }
}
