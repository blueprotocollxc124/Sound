package org.linkworld.yuansystem.service;

/*
 *@Author  LXC BlueProtocol
 *@Since   2022/2/17
 */


import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
import org.linkworld.yuansystem.model.entity.StudentCourse;

import java.math.BigInteger;

public interface StudentCourseService extends IService<StudentCourse> {
    String getMyCourse(BigInteger studentId);

    Boolean addCourse(BigInteger studentId, String stuCourses);
}
