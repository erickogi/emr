package com.kogi.emr.payload.request;

import com.kogi.emr.models.OrderStatus;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class OrderStatusFormatValidator implements ConstraintValidator<OrderStatusFormat, String> {
    @Override
    public void initialize(OrderStatusFormat constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Null values are handled by @NotNull or other annotations
        }

        boolean isValid =  value.equals(OrderStatus.PENDING.name()) || value.equals(OrderStatus.PAID.name()) || value.equals(OrderStatus.CANCELLED.name());

        System.out.println(isValid);
        return  isValid;
    }
}
