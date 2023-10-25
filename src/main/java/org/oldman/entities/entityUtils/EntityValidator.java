package org.oldman.entities.entityUtils;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.ws.rs.NotFoundException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Set;

public class EntityValidator {
    // to prevent instantiation of class inside or outside of it
    private EntityValidator() {
        throw new AssertionError();
    }

//    TODO: replace console output on logging
    public static <T> void validateEntityBeforeSave(T entity) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

        //It validates bean instances
        Validator validator = factory.getValidator();

        //Validate bean
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(entity);

        //Show errors
        if (constraintViolations.size() > 0) {
            for (ConstraintViolation<T> violation : constraintViolations) {
                System.out.println(violation.getMessage());
            }
        } else {
            System.out.println("Valid Object");
        }
    }

    public static <T> T returnOrThrowIfNull(T entity, String errorMessage) {
        if (entity == null) {
            throw new NotFoundException(errorMessage);
        }
        return entity;
    }
}
