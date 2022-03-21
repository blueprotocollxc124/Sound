package org.linkworld.yuansystem.model.vo;

/*
 *@Author  LXC BlueProtocol
 *@Since   2022/2/16
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class WorkVo {

 private String workName;

 private String WorkDetail;
}
