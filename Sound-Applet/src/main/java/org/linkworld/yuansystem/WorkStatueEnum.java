package org.linkworld.yuansystem;

/*
 *@Author  LXC BlueProtocol
 *@Since   2022/2/16
 */

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum WorkStatueEnum {

 HAS_NOT_FINISH(1,"未完成"),

 ALREADY_TIME_OUT(2,"已过期"),

 WAIT_SENTENCE(3,"待批阅"),

 HAS_FINISH(4,"已完成"),

 HAS_FINISH_SENTENCE(5,"已经批阅");



 private int statue;

 private String description;


 public static WorkStatueEnum getWorkStatueEnumByKey(int key) {
  for (WorkStatueEnum workStatueEnum : WorkStatueEnum.values()) {
   if(workStatueEnum.statue == key) {
    return workStatueEnum;
   }
  }
  return null;
 }
}
