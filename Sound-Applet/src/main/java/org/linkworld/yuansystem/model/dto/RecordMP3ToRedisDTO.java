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
import org.linkworld.yuansystem.check.PhoneCheck;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecordMP3ToRedisDTO {

    @NotNull(message = "课程id不能为空")
    @ApiModelProperty(value = "课程id",required = true)
    private BigInteger courseId;

    @NotNull(message = "作业id不能为空")
    @ApiModelProperty(value = "作业id",required = true)
    private BigInteger workId;

    @NotNull(message = "录音的类型不能为空")
    @ApiModelProperty(value = "录音的类型，如元音a为1",required = true)
    private Integer type;

    @NotNull(message = "要上传的文件")
    @ApiModelProperty(value = "要上传的文件",required = true)
    private MultipartFile file;


}
