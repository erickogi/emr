package com.kogi.emr.payload.request;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = OrderStatusFormatValidator.class)
@Documented
public @interface OrderStatusFormat {
    String message() default "Invalid format. Should be either Paid, Cancelled and Pending";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}