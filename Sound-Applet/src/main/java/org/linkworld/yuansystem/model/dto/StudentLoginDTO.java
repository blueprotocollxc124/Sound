package org.linkworld.yuansystem.model.dto;

/*
 *@Author  LXC BlueProtocol
 *@Since   2022/2/27
 */


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.linkworld.yuansystem.check.PasswordCheck;
import org.linkworld.yuansystem.check.PhoneCheck;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentLoginDTO {

 @ApiModelProperty(value = "手机号",required = true)
 @NotEmpty(message = "输入的手机号不能为空",groups = {PhoneCheck.class})
 @Length(min=0,max = 255,message = "输入手机号的长度应在{min}-{max}之间")
 private String phone;

 @ApiModelProperty(value = "密码",required = true)
 @NotEmpty(message = "输入的密码不能为空",groups = {PasswordCheck.class})
 @Length(min=0,max = 255,message = "输入手机号的长度应在{min}-{max}之间")
 private String password;

 @NotNull(message = "code不能为空")
 private String code;
}
