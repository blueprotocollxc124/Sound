package org.linkworld.yuansystem.model.vo;

/*
 *@Author  LXC BlueProtocol
 *@Since   2022/2/16
 */


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.linkworld.yuansystem.model.entity.Course;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class CourseVO {

 private String courseId;

 private String courseName;

 private String courseTeacher;

 private String headAddr;



}
