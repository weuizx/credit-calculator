package edu.t1.calculator.controller.dto.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DataValidator implements ConstraintValidator<ValidData, LocalDateTime> {

    private LocalDateTime minDateTime;
    private LocalDateTime maxDateTime;

    @Override
    public void initialize(ValidData constraintAnnotation) {
        this.minDateTime = LocalDateTime.parse(constraintAnnotation.min(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        this.maxDateTime = LocalDateTime.parse(constraintAnnotation.max(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return (value.isEqual(minDateTime) || value.isAfter(minDateTime)) &&
                (value.isEqual(maxDateTime) || value.isBefore(maxDateTime));
    }
}
