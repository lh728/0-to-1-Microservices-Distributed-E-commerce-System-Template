package com.ecommercesystemtemplate.product.exception;

import com.ecommercesystemtemplate.common.exception.BizCodeEnume;
import com.ecommercesystemtemplate.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@Slf4j
@RestControllerAdvice(basePackages = "com.ecommercesystemtemplate.product.controller")
public class ExceptionControllerAdvice {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R exceptionHandler(MethodArgumentNotValidException e) {
        HashMap<String, String> errorMap = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach((fieldError) -> {
            errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
        });
        return R.error(BizCodeEnume.VALID_EXCEPTION.getCode(), BizCodeEnume.VALID_EXCEPTION.getMessage()).put("data", errorMap);
    }

    /**
     * all  exception default handling
     */
    @ExceptionHandler(value = Throwable.class)
    public R exceptionHandler(Throwable e) {
        log.error("Exception: ", e);
        return R.error(BizCodeEnume.UNKNOWN_EXCEPTION.getCode(), BizCodeEnume.UNKNOWN_EXCEPTION.getMessage());
    }


}
