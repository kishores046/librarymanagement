package com.chinna.librarymanagement.validatorsutil.valid;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.chinna.librarymanagement.validatorsutil.Validators.ISBNValidator;

import java.lang.annotation.ElementType;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = ISBNValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidISBN {

    String message() default "Invalid ISBN number";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
