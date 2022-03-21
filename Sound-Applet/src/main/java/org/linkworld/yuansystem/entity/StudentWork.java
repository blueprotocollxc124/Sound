package org.linkworld.yuansystem.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
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
 * @since 2022-03-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="StudentWork对象", description="")
public class StudentWork implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "学生id")
    private Long stuId;

    @ApiModelProperty(value = "课程id")
    private Long courseId;

    @ApiModelProperty(value = "作业名称")
    private String  name;

    @ApiModelProperty(value = "作业id")
    private Long workId;

    @ApiModelProperty(value = "作业状态")
    private Integer status;

    @ApiModelProperty(value = "分数")
    private Integer score;

    @ApiModelProperty(value = "教师评语")
    private String teacherComment;

    private String picturePath;

    @JsonFormat(locale="zh",timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @JsonFormat(locale="zh",timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;


}
