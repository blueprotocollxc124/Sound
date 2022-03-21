package org.linkworld.yuansystem.model.dto;

/*
 *@Author  LXC BlueProtocol
 *@Since   2022/3/3
 */

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.linkworld.yuansystem.model.entity.Email;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendEmailDTO {

 @NotNull(message = "课程id不能为空")
 @ApiModelProperty(value = "课程id",required = true)
 private BigInteger courseId;


 @NotNull(message = "邮件对象不能为空")
 @ApiModelProperty(value = "邮件对象",required = true)
 private Email email;
}
