package org.linkworld.yuansystem.config;

/*@Function 代码自动生成包的设置
 *@Author  LiuXiangCheng
 *@Since   2021/12/2  10:41
 */


import com.baomidou.mybatisplus.generator.config.PackageConfig;
import org.linkworld.yuansystem.model.entity.entity.YuanPackage;

public class YuanPackageConfig extends PackageConfig {

 /**
  * 代码自动生成包的设置
  * @param parentPackage 父包
  * @param packageModule 模块包
  * @return PackageConfig
  */
 @Deprecated
 public static PackageConfig getPackageConfig(String parentPackage, String packageModule) {
  PackageConfig packageConfig = new PackageConfig();
  packageConfig.setParent("org.linkworld");
  packageConfig.setModuleName("yuansystem");
  packageConfig.setEntity("entity");
  packageConfig.setMapper("dao");
  packageConfig.setService("service");
  packageConfig.setController("controller");
  return packageConfig;
 }

 /**
  * 代码自动生成包的设置
  * @param aPackage YuanPackage对象
  * @return PackageConfig对象
  */
 public static PackageConfig getPackageConfig(YuanPackage aPackage) {
  return getPackageConfig(aPackage.getParentPackage(),aPackage.getPackageModule());
 }

}
