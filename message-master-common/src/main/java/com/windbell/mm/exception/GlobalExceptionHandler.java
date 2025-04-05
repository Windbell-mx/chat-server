package com.windbell.mm.exception;

import com.windbell.mm.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * 全局异处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Result<String> exc(Exception e) {
        log.error(e.getMessage(), e);
        return Result.fail(e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(MessageException.class)
    public Result<String> exc(MessageException e){
        log.error(e.getMessage(), e);
        return Result.fail(e.getCode(), e.getMessage());
    }
}
