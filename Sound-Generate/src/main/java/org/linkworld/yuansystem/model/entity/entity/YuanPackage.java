package org.linkworld.yuansystem.model.entity.entity;

/*@Function  包配置实体
 *@Author  LiuXiangCheng
 *@Since   2021/12/2  11:11
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class YuanPackage {
    /**
     * 父包
     */
    private String parentPackage;

    /**
     * 模块包
     */
    private String packageModule;
}
