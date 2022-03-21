package org.linkworld.yuansystem.model.dto;

/*
 *@Author  LXC BlueProtocol
 *@Since   2022/3/19
 */


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.File;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class RecordDTO {

 private String workId;

 private String code;

 private File file;
}
