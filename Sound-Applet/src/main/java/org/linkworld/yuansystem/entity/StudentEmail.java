package org.linkworld.yuansystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;

import java.math.BigInteger;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
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
 * @since 2022-03-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="StudentEmail对象", description="")
public class StudentEmail implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "学生id")
    private BigInteger stuId;

    @ApiModelProperty(value = "邮件id")
    private BigInteger emailId;


    @ApiModelProperty(value = "邮件状态，0表示未读")
    private Integer statue;


}
