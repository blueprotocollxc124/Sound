package org.linkworld.yuansystem.config;

/*代码自动生成数据源配置
 *@Author  LiuXiangCheng
 *@Since   2021/12/2  10:24
 */


import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import org.linkworld.yuansystem.model.entity.entity.YuanDataSource;

public class YuanDataSourceConfig {

 /**
  *配置代码自动生成的数据源配置
  * @param username 数据库用户名
  * @param password 数据库密码
  * @param DriverName 数据库驱动类型
  * @param url 数据库url
  * @return DataSourceConfig对象
  */
 @Deprecated
 public static DataSourceConfig getDataSourceConfig(String username, String password, String DriverName, String url) {
  DataSourceConfig dataSourceConfig = new DataSourceConfig();
  dataSourceConfig.setUsername(username);
  dataSourceConfig.setPassword(password);
  dataSourceConfig.setDriverName(DriverName);
  dataSourceConfig.setUrl(url);
  return dataSourceConfig;
 }

 /**
  * 配置代码自动生成的数据源配置
  * @param dataSource YuanDataSource对象
  * @return DataSourceConfig对象
  */
 public static DataSourceConfig getDataSourceConfig(YuanDataSource dataSource) {
  return getDataSourceConfig(dataSource.getUsername(),
          dataSource.getPassword(),
          dataSource.getDriverClassName(),
          dataSource.getUrl());
 }

}
