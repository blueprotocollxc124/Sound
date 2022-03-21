package org.linkworld.yuansystem.model.entity.entity;

/*@Function  策略配置实体
 *@Author  LiuXiangCheng
 *@Since   2021/12/2  12:53
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class YuanStrategy {

    /**
     * 要映射的表名，可变形参个数
     */
    private String[] tableName;

    /**
     * 乐观锁字段
     */
    private String VersionFieldName;

    /**
     * 逻辑删除字段名
     */
    private String logicDeleteFieldName;

    /**
     * 是否开启lombok
     */
    private Boolean isLombok;

    /**
     * 是否在Controller层中使用RestFul风格
     */
    private Boolean isRestfulStyle;
}
