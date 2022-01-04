package com.yeqk97.gulimall.product.exception;

import com.yeqk97.common.exception.BizCodeEnum;
import com.yeqk97.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice(basePackages = "com.yeqk97.gulimall.product.controller")
public class GulimallExceptionControllerAdvice {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handleValidException(MethodArgumentNotValidException e) {
        log.error("Error validating data: {}. exception type:{}. ",
                e.getMessage(), e.getClass());
        BindingResult bindingResult = e.getBindingResult();

        Map<String, String> map = bindingResult.getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage));
        return R.error(BizCodeEnum.VALID_EXCEPTION.getCode(),
                BizCodeEnum.VALID_EXCEPTION.getMsg()).put("data", map);
    }

    @ExceptionHandler(value = Throwable.class)
    public R handleException(Exception e) {
        log.error("Error", e);
        return R.error(BizCodeEnum.UNKNOWN_EXCEPTION.getCode(),
                BizCodeEnum.UNKNOWN_EXCEPTION.getMsg());
    }
}
