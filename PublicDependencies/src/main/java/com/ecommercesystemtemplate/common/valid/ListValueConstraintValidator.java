package com.ecommercesystemtemplate.common.valid;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.HashSet;
import java.util.Set;

public class ListValueConstraintValidator implements ConstraintValidator<ListValue, Integer> {

    private Set<Integer> set = new HashSet<>();
    @Override
    public void initialize(ListValue constraintAnnotation) {

        int[] value = constraintAnnotation.value();
        for (int i : value) {
            set.add(i);
        }
    }

    /**
     * @param integer submitted value
     * @param constraintValidatorContext constraintValidatorContext
     * @return boolean
     */
    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext constraintValidatorContext) {
        return set.contains(integer);
    }
}
