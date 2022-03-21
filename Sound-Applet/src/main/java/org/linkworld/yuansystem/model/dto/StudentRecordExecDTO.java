package org.linkworld.yuansystem.model.dto;

/*
 *@Author  LXC BlueProtocol
 *@Since   2022/3/11
 */

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentRecordExecDTO {
    @ApiModelProperty(name = "execId",required = true)
    @NotNull(message = "练习的id不能为空")
    BigInteger execId;

    @NotNull(message = "录音的类型不能为空")
    @ApiModelProperty(value = "录音的类型，如元音a为1",required = true)
    private Integer code;

}
