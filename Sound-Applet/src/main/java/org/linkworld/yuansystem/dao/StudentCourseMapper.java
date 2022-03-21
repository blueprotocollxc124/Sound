package org.linkworld.yuansystem.dao;

/*
 *@Author  LXC BlueProtocol
 *@Since   2022/2/15
 */


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.linkworld.yuansystem.model.entity.StudentCourse;

import java.math.BigInteger;

@Mapper
public interface StudentCourseMapper  extends BaseMapper<StudentCourse> {

 @Select("select stu_courses from student_course where stu_id = #{studentId}")
  String getMyCourse(@Param("studentId") BigInteger studentId);

 @Update("update student_course set stu_courses = #{stuCourses} where stu_id = #{studentId}")
  Boolean addCourse(@Param("studentId") BigInteger studentId, @Param("stuCourses")String stuCourses);


}
