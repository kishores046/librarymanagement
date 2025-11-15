package com.chinna.librarymanagement.validatorsutil.Validators;

import com.chinna.librarymanagement.validatorsutil.valid.ValidISBN;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ISBNValidator implements ConstraintValidator<ValidISBN, String> {

    @Override
    public void initialize(ValidISBN constraintAnnotation) {
        // Optional: if you need to read properties from annotation
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true; // use @NotNull separately if needed
        }

        // Simple ISBN-10 or ISBN-13 check
        return value.matches("^(97(8|9))?\\d{9}(\\d|X)$");
    }
}
