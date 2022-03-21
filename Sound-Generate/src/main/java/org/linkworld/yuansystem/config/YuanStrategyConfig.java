package org.linkworld.yuansystem.config;

/*@Function 策略的配置
 *@Author  LiuXiangCheng
 *@Since   2021/12/2  10:45
 */


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import org.linkworld.yuansystem.model.entity.entity.YuanStrategy;

import java.util.ArrayList;

public class YuanStrategyConfig extends StrategyConfig {

 /**
  * 策略的配置
  * @param tableName 要映射的数据表名，可变形参个数
  * @param VersionFieldName 乐观锁字段名
  * @param logicDeleteFieldName 逻辑删除字段名
  * @param isLombok 是否开启lombok
  * @param isRestfulStyle 是否开启控制层RestFull风格
  * @return StrategyConfig对象
  */
 @Deprecated
 public static StrategyConfig getStrategyConfig(String[] tableName, String VersionFieldName, String logicDeleteFieldName,
                                                Boolean isLombok, Boolean isRestfulStyle) {
  StrategyConfig strategyConfig = new StrategyConfig();
  // 这个非常重要，设置要映射的数据库表名，可变形参个数
  strategyConfig.setInclude(tableName);
  // 名字驼峰命名
  strategyConfig.setNaming(NamingStrategy.underline_to_camel);
  // 字段驼峰命名
  strategyConfig.setColumnNaming(NamingStrategy.underline_to_camel);
  // 逻辑删除
  strategyConfig.setLogicDeleteFieldName(logicDeleteFieldName);
  ArrayList<TableFill> tableFillList = new ArrayList<>();//自动填充
  TableFill tableFill1 = new TableFill("gmtCreate", FieldFill.INSERT);
  TableFill tableFill2 = new TableFill("gmtModified", FieldFill.INSERT_UPDATE);
  tableFillList.add(tableFill1);
  tableFillList.add(tableFill2);
  strategyConfig.setTableFillList(tableFillList );
  // 是否开启lombok
  strategyConfig.setEntityLombokModel(isLombok);
  // 配置乐观锁字段名
  strategyConfig.setVersionFieldName(VersionFieldName);
  // 是否开启ControllerRestFull风格
  strategyConfig.setRestControllerStyle(isRestfulStyle);
  // localhost:8080/hello_id_1
  strategyConfig.setControllerMappingHyphenStyle(true);
  return strategyConfig;
 }


 /**
  * 策略的配置
  * @param strategy YuanStrategy对象
  * @return StrategyConfig
  */
 public static StrategyConfig getStrategyConfig(YuanStrategy strategy) {
  return getStrategyConfig(strategy.getTableName(),
          strategy.getVersionFieldName(),
          strategy.getVersionFieldName(),
          strategy.getIsLombok(),
          strategy.getIsRestfulStyle());
 }

}
