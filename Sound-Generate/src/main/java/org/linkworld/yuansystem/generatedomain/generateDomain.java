package org.linkworld.yuansystem.generatedomain;

/*代码自动生成入口
 *@Author  LiuXiangCheng
 *@Since   2021/12/2  11:00
 */


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import org.linkworld.yuansystem.config.YuanDataSourceConfig;
import org.linkworld.yuansystem.config.YuanGlobalConfig;
import org.linkworld.yuansystem.config.YuanPackageConfig;
import org.linkworld.yuansystem.config.YuanStrategyConfig;
import org.linkworld.yuansystem.model.entity.entity.YuanDataSource;
import org.linkworld.yuansystem.model.entity.entity.YuanGlobal;
import org.linkworld.yuansystem.model.entity.entity.YuanPackage;
import org.linkworld.yuansystem.model.entity.entity.YuanStrategy;

public class generateDomain {
 public static void main(String[] args) {

  // 设置数据库配置
  YuanDataSource dataSource = new YuanDataSource()
          .setUsername("root")
          .setPassword("123456")
          .setDriverClassName("com.mysql.jdbc.Driver")
          .setUrl("jdbc:mysql://localhost:3306/sound?useSSL=true&useUnicode=true&characterEncoding=utf-8&serverTimeZone=UTC");

  // 设置全局配置
  YuanGlobal global = new YuanGlobal()
          .setAuth("BlueProtocol")
          .setIdTpe(IdType.ID_WORKER)
          .setModuleName("Sound-Applet")
          .setOpenSwagger(true);

  // 设置包的配置
  YuanPackage aPackage = new YuanPackage()
          .setParentPackage("org.linkworld")
          .setPackageModule("yuansystem");

  // 设置策略的配置
  YuanStrategy strategy = new YuanStrategy()
          .setTableName(new String[]{"exec","student_exec"})
          .setVersionFieldName("version")
          .setLogicDeleteFieldName("deleted")
          .setIsLombok(true)
          .setIsRestfulStyle(true);

  // 设置总配置
  DataSourceConfig sourceConfig = YuanDataSourceConfig.getDataSourceConfig(dataSource);
  GlobalConfig globalConfig = YuanGlobalConfig.getGlobalConfig(global);
  PackageConfig packageConfig = YuanPackageConfig.getPackageConfig(aPackage);
  StrategyConfig strategyConfig = YuanStrategyConfig.getStrategyConfig(strategy);

  // 配置生成器
  AutoGenerator generator = new AutoGenerator()
          .setDataSource(sourceConfig)
          .setGlobalConfig(globalConfig)
          .setPackageInfo(packageConfig)
          .setStrategy(strategyConfig);

  // 执行生成
  generator.execute();



 }
}
