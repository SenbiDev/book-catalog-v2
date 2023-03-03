package com.subrutin.catalog.validator.annotation;

import com.subrutin.catalog.validator.AuthorNameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(FIELD)
@Constraint(validatedBy = AuthorNameValidator.class)
public @interface ValidAuthorName {

    String message() default "author name invalid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
