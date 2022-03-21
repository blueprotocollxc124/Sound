package org.linkworld.yuansystem.model.vo;

/*
 *@Author  LXC BlueProtocol
 *@Since   2022/2/14
 */


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ResultBean {

    @ApiModelProperty("响应码")
    private Integer code;

    @ApiModelProperty("信息")
    private String message;


    @ApiModelProperty("是否成功")
    private Boolean success;

    @ApiModelProperty("返回数据")
    private Object data;


    public static ResultBean ok() {
        ResultBean resultBean = new ResultBean();
        resultBean
                .setCode(ResultBeanEnum.RESULT_OK.getCode())
                .setMessage(ResultBeanEnum.RESULT_OK.getMessage())
                .setSuccess(ResultBeanEnum.RESULT_OK.getSuccess());
        return resultBean;
    }

    public static ResultBean bad() {
        ResultBean resultBean = new ResultBean();
        resultBean
                .setCode(ResultBeanEnum.RESULT_FAILURE.getCode())
                .setMessage(ResultBeanEnum.RESULT_FAILURE.getMessage())
                .setSuccess(ResultBeanEnum.RESULT_FAILURE.getSuccess());
        return resultBean;
    }
}
