package org.linkworld.yuansystem.model.dto;

/*
 *@Author  LXC BlueProtocol
 *@Since   2022/3/2
 */


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkDetailDTO {
    @NotNull(message = "课程id不能为空")
    @ApiModelProperty(value = "课程id",required = true)
    private BigInteger courseId;

    @NotNull(message = "作业id不能为空")
    @ApiModelProperty(value = "作业id",required = true)
    private BigInteger workId;
}
