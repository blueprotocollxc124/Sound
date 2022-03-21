package org.linkworld.yuansystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * @since 2022-03-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Exec对象", description="")
public class Exec implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "练习id")
    @TableId(value = "exec_id", type = IdType.ID_WORKER)
    private Long execId;

    @ApiModelProperty(value = "练习名")
    private String name;

    @ApiModelProperty(value = "练习内容")
    private String content;

    @ApiModelProperty(value = "练习的图片的位置")
    private String picture;


}
