package org.linkworld.yuansystem.exception;

/*
 *@Author  LXC BlueProtocol
 *@Since   2022/2/14
 */


public class NullParamException extends BadRequestException{

 public NullParamException(String message) {
  super(message);
 }

 public NullParamException(String message, Throwable cause) {
  super(message, cause);
 }
}
