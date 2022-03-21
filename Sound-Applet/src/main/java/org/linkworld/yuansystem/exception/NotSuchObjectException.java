package org.linkworld.yuansystem.exception;

/*
 *@Author  LXC BlueProtocol
 *@Since   2022/2/16
 */


public class NotSuchObjectException extends BadRequestException{

 public NotSuchObjectException(String message) {
  super(message);
 }

 public NotSuchObjectException(String message, Throwable cause) {
  super(message, cause);
 }
}
