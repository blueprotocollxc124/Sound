package org.linkworld.yuansystem.exception;

/*
 *@Author  LiuXiangCheng
 *@Since   2021/12/5  18:23
 */


import lombok.NonNull;
import org.springframework.http.HttpStatus;

public abstract class AbstractSoundException extends RuntimeException{

 private Object errorData;

 public AbstractSoundException(String message) {
  super(message);
 }

 public AbstractSoundException(String message, Throwable cause) {
  super(message, cause);
 }


 public abstract HttpStatus getHttpStatus();


 public Object getErrorData() {
  return errorData;
 }


 public AbstractSoundException setErrorData(Object errorData) {
  this.errorData = errorData;
  return this;
 }

}
