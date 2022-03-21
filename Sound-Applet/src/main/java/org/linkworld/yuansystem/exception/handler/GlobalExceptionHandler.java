package org.linkworld.yuansystem.exception.handler;

/*
 *@Author  LiuXiangCheng
 *@Since   2021/12/5  18:46
 */

import lombok.extern.slf4j.Slf4j;
import org.linkworld.yuansystem.exception.BadRequestException;
import org.linkworld.yuansystem.model.vo.ResultBean;
import org.linkworld.yuansystem.util.ThrowableUtil;
import org.springframework.core.MethodParameter;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity badRequestException(BadRequestException badRequestException) {
        System.out.println("出错误了");
        log.error(badRequestException.getMessage());
        /**
         * 打印堆栈信息
         */
        log.error(ThrowableUtil.getStackTrance(badRequestException));
        return new ResponseEntity(badRequestException,badRequestException.getHttpStatus());
    }

    @ExceptionHandler(value= MethodArgumentNotValidException.class)
    @ResponseBody
    public ResultBean exception(MethodArgumentNotValidException e) {
        // 输出所有的错误信息
        List<ObjectError> errors = e.getBindingResult().getAllErrors();
        return ResultBean.bad().setMessage(errors.get(0).getDefaultMessage());
    }

    @ExceptionHandler(value= ValidationException.class)
    @ResponseBody
    public String validation(ValidationException e) {
        ThrowableUtil.getStackTrance(e);
        log.error(e.getMessage());
        System.out.println("va错误");
        return "sdfsdf";
    }

    @ExceptionHandler(value= ConstraintViolationException.class)
    @ResponseBody
    public String validation(ConstraintViolationException e) {
        ThrowableUtil.getStackTrance(e);
        log.error(e.getMessage());
        System.out.println("Co错误");
        return "sdfsdf";
    }
}
