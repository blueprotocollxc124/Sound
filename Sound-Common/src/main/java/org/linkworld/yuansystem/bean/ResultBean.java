package org.linkworld.yuansystem.bean;

/*
 *@Author  LiuXiangCheng
 *@Since   2022/1/18  21:15
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultBean<T>{
 private int code;
 private String message;
 private T data;
}
