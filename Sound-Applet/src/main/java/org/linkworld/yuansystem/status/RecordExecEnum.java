package org.linkworld.yuansystem.status;

/*
 *@Author  LXC BlueProtocol
 *@Since   2022/3/11
 */


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum RecordExecEnum {
 RECORD_EXEC_A(1,"元音a"),
 RECORD_EXEC_E(2,"元音i"),
 RECORD_EXEC_I(3,"元音u"),
 RECORD_EXEC_O(4,"唱段")
 ;

 public static RecordExecEnum getExecEnumByType(Integer typeCode) {
  for (RecordExecEnum value : RecordExecEnum.values()) {
   if(value.getCode() == typeCode) {
    return value;
   }
  }
  return null;
 }


 private Integer code;
 private String describe;
}
