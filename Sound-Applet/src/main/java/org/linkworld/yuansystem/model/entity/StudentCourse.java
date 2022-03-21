package org.linkworld.yuansystem.model.entity;

/*
 *@Author  LXC BlueProtocol
 *@Since   2022/2/17
 */


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentCourse {

    private BigInteger stuId;

    private String stuCourses;
}
