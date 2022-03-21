package org.linkworld.yuansystem.model.dto;

/*
 *@Author  LXC BlueProtocol
 *@Since   2022/2/28
 */

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.linkworld.yuansystem.check.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentRegisterDTO {

 @NotBlank(message = "名称不能为空",groups = {NameCheck.class})
 @ApiModelProperty(value = "学生id",required = true)
 @Length(min = 0,max = 255,message = "名称长度必须在{minx}-{max}之间",groups = {NameCheck.class})
 private String name;

 @NotBlank(message = "密码不能为空",groups = {PasswordCheck.class})
 @ApiModelProperty(value = "学生id",required = true)
 @Length(min = 0,max = 255,message = "密码长度必须在{minx}-{max}之间",groups = {PasswordCheck.class})
 private String password;

 @NotBlank(message = "手机号码不能为空",groups={PhoneCheck.class})
 @ApiModelProperty(value = "手机号码",required = true)
 @Pattern(regexp = "^1(3|4|5|7|8)\\d{9}$", message = "手机号码格式错误",groups={PhoneCheck.class})
 private String phone;


 @Email(message = "邮箱格式错误",groups={EmailCheck.class})
 @ApiModelProperty(value = "邮箱",required = true)
 private String email;

 @NotBlank(message = "组织码不能为空",groups={GroupNumCheck.class})
 @ApiModelProperty(value = "组织码",required = true)
 @Length(min = 0,max = 255,message = "组织码长度必须在{minx}-{max}之间",groups={GroupNumCheck.class})
 private String groupNum;
}
