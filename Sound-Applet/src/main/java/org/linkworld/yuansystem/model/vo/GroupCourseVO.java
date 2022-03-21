package org.linkworld.yuansystem.model.vo;

/*
 *@Author  LXC BlueProtocol
 *@Since   2022/3/4
 */


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.linkworld.yuansystem.model.entity.Course;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigInteger;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class GroupCourseVO {


 @NotNull(message = "课程id不能为空")
 private BigInteger courseId;

 @NotBlank(message = "课程名称不为空")
 @Size(min = 0, max = 255,message = "课程名称的长度应在{min}-{max}之间")
 private String courseName;

 public GroupCourseVO(Course course) {
  this.courseId = course.getCourseId();
  this.courseName = course.getCourseName();
 }
}
