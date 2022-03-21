package org.linkworld.yuansystem.exception;

/*
 *@Author  LiuXiangCheng
 *@Since   2021/12/5  18:44
 */


import org.springframework.http.HttpStatus;

public class BadRequestException extends AbstractSoundException{

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }


}
