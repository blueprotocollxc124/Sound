package org.linkworld.yuansystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.math.BigInteger;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author BlueProtocol
 * @since 2022-03-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="StudentExec对象", description="")
public class StudentExec implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "学生id")
    private BigInteger studentId;

    @ApiModelProperty(value = "练习id")
    private BigInteger execId;

    @ApiModelProperty(value = "练习的类型")
    private Integer code;

    @ApiModelProperty(value = "学生练习的分数")
    private Integer score;

    @ApiModelProperty(value = "文件保存的路径")
    private String filePath;

    @ApiModelProperty(value = "学生原文件的名称")
    private String sourceFileName;



}
