package com.bko.validators;


import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.bko.domain.User;

public class FileValidator implements Validator {
	public boolean supports(Class clazz) {
		return User.class.equals(clazz);
	}

	public void validate(Object obj, Errors errors) {
		
		User user = (User)obj;
		if (user == null || user.getFileToFormat() == null){
			errors.rejectValue("fileToFormat", "validate.file.type");
			return;
		}
		if (!user.getFileToFormat().getOriginalFilename().endsWith(".csv")) {
		    errors.rejectValue("fileToFormat", "validate.file.type");
		  }
	}

}
