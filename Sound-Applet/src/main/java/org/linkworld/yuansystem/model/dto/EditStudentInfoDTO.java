package org.linkworld.yuansystem.model.dto;

/*
 *@Author  LXC BlueProtocol
 *@Since   2022/3/3
 */

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditStudentInfoDTO {

    @ApiModelProperty(value = "性别", required = true)
    private Integer sex;

    @NotEmpty(message = "昵称不能为空")
    @Size(min = 0, max = 255, message = "昵称的长度应在{min}-{max}之间")
    @ApiModelProperty(value="用户昵称",required = true)
    private String name;

    @Size(min = 0, max = 255, message = "昵称的长度应在{min}-{max}之间")
    @ApiModelProperty(value = "个性签名", required = true)
    private String signature;



}
