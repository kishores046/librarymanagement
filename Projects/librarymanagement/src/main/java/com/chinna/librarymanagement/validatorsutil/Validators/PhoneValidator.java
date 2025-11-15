package com.chinna.librarymanagement.validatorsutil.Validators;

import com.chinna.librarymanagement.validatorsutil.valid.ValidPhone;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<ValidPhone,String>{

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if(value==null || value.isEmpty()) {
			return false;
		}
		return value.matches("^\\d{10}$");
	}

}
