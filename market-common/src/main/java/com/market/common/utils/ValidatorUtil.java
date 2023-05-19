package com.market.common.utils;

import com.market.common.exception.ServiceException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * 自定义校验工具
 */
public class ValidatorUtil {

    private static Validator validator;

    static {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    /**
     * 校验对象
     *
     * @param object 待校验的对象
     * @param groups 待校验的组
     * @throws ServiceException 校验不通过抛出业务异常
     */
    public static void validateEntity(Object object, Class<?>... groups) {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, groups);
        if (!constraintViolations.isEmpty()) {
            constraintViolations.forEach(o -> {
                throw new ServiceException(o.getMessage());
            });
        }
    }

}
