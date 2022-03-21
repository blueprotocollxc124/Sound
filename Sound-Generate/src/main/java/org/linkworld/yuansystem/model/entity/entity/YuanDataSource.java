package org.linkworld.yuansystem.model.entity.entity;

/*@Function  数据源对象
 *@Author  LiuXiangCheng
 *@Since   2021/12/2  11:03
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class YuanDataSource {

 /**
  * 数据库用户名
  */
 private String username;


 /**
  * 数据库密码
  */
 private String password;

 /**
  * 数据库驱动
  */
 private String driverClassName;

 /**
  * 数据库URL
  */
 private String url;

}
