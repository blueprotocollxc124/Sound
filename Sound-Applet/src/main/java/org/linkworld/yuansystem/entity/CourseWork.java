package org.linkworld.yuansystem.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;

import java.math.BigInteger;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author BlueProtocol
 * @since 2022-02-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "CourseWork对象", description = "")
@NoArgsConstructor
@AllArgsConstructor
public class CourseWork implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigInteger courseId;

    private BigInteger workId;

    private String name;

    private String workDescribe;

    private Integer status;

    private String picturePath;

    @JsonFormat(locale="zh",timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @JsonFormat(locale = "zh" , timezone = "GMT+8" ,pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;


}
