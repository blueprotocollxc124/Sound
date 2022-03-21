package org.linkworld.yuansystem.model.entity.entity;

/*@Function  全局配置实体
 *@Author  LiuXiangCheng
 *@Since   2021/12/2  11:07
 */

import com.baomidou.mybatisplus.annotation.IdType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class YuanGlobal {

 /**
  * 模块名
  */
 private String moduleName;

 /**
  * 自动生成的ID类型
  */
 private IdType idTpe;

 /**
  * 是否开启Swagger
  */
 private Boolean openSwagger;

 /**
  * 作者
  */
 private String auth;

}
