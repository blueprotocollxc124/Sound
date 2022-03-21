package org.linkworld.yuansystem.config;

/*@Function  配置策略
 *@Author  LiuXiangCheng
 *@Since   2021/12/2  9:31
 */


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import org.linkworld.yuansystem.model.entity.entity.YuanGlobal;

public class YuanGlobalConfig extends GlobalConfig {

 /**
  * 代码自动生成的配置策略
  * @param moduleName 模块名
  * @param idTpe 自动生成的ID类型
  * @param openSwagger 是否开启日志
  * @param auth 作者
  * @return GlobalConfig对象
  */
 @Deprecated
 public static GlobalConfig getGlobalConfig(String moduleName, IdType idTpe, Boolean openSwagger, String auth) {
  GlobalConfig gc = new GlobalConfig();
  String projectPath = System.getProperty("user.dir");
  gc.setOutputDir(projectPath+"/"+moduleName+"/src/main/java");
  gc.setAuthor(auth);
  // 设置不打开我的电脑
  gc.setOpen(false);
  // 设置是否覆盖原有的东西
  gc.setFileOverride(false);
  // 用正则表达式去掉Service的I前缀
  gc.setServiceName("%sService");
  // 设置全局唯一id，雪花算法
  gc.setIdType(idTpe);
  gc.setDateType(DateType.ONLY_DATE);
  // 设置日志
  gc.setSwagger2(openSwagger);
  return gc;
 }

 /**
  * 代码自动生成的配置策略
  * @param global YuanGlobal对象
  * @return GlobalConfig对象
  */
 public static GlobalConfig getGlobalConfig(YuanGlobal global) {
  return getGlobalConfig(global.getModuleName(),
          global.getIdTpe(),
          global.getOpenSwagger(),
          global.getAuth());
 }

}
