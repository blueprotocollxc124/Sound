package org.linkworld.yuansystem.model.dto;

/*
 *@Author  LXC BlueProtocol
 *@Since   2022/3/1
 */


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetRecordFromRedisDTO {


 @NotNull(message = "课程id不能为空")
 @ApiModelProperty(value = "课程id",required = true)
 private BigInteger courseId;

 @NotNull(message = "作业id不能为空")
 @ApiModelProperty(value = "作业id",required = true)
 private BigInteger workId;

 @NotNull(message = "录音的类型不能为空")
 @ApiModelProperty(value = "录音的类型，如元音a为1",required = true)
 private Integer type;


}
