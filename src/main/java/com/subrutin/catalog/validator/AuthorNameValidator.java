package com.subrutin.catalog.validator;

import com.subrutin.catalog.validator.annotation.ValidAuthorName;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

// kelas digunakan untuk membuat kustom validasi
// ConstraintValidator<NamaAnotasiValidasi yang dibuat, Tipe dari property yang diberi anotasi>
@Component
public class AuthorNameValidator implements ConstraintValidator<ValidAuthorName, String> {

    @Override
    public boolean isValid(String authorName, ConstraintValidatorContext constraintValidatorContext) {
        return !authorName.equalsIgnoreCase("Tedy");
    }
}
