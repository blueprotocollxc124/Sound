package org.linkworld.yuansystem.model.vo;

/*
 *@Author  LXC BlueProtocol
 *@Since   2022/2/14
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum ResultBeanEnum {

 RESULT_OK(200,"成功",true),

 RESULT_FAILURE(500,"失败",false)

 ;
 private Integer code;

 private String message;

 private Boolean success;

}
