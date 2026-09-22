package upm.app.presentation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class ValidationHandler {
    private final Validator validator;

    public ValidationHandler(Validator validator) {
        this.validator = validator;
    }

    public <T> void validate(T entity) {
        Set<ConstraintViolation<T>> violations = this.validator.validate(entity);
        if (!violations.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder("Validation failed: ");
            for (ConstraintViolation<T> violation : violations) {
                errorMessage.append(violation.getPropertyPath()).append(" ").append(violation.getMessage()).append("; ");
            }
            throw new IllegalArgumentException(errorMessage.toString());
        }
    }
}
