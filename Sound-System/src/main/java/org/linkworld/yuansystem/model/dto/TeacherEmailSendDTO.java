package org.linkworld.yuansystem.model.dto;

/*
 *@Author  LXC BlueProtocol
 *@Since   2022/3/5
 */


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherEmailSendDTO {

 @NotBlank(message = "邮件标题不能为空")
 @Size(min = 0, max= 255,message = "邮件的标题的字符数应该在{min}-{max}之间")
 private String title;

 @NotBlank(message = "邮件标题不能为空")
 @Size(min = 0, max= 500,message = "邮件的标题的字符数应该在{min}-{max}之间")
 private String content;
}
