package org.linkworld.yuansystem.service.impl;

/*
 *@Author  LXC BlueProtocol
 *@Since   2022/2/17
 */


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.linkworld.yuansystem.dao.StudentCourseMapper;
import org.linkworld.yuansystem.model.entity.StudentCourse;
import org.linkworld.yuansystem.service.StudentCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class StudentCourseServiceImpl extends ServiceImpl<StudentCourseMapper, StudentCourse> implements StudentCourseService {

    @Autowired
    private StudentCourseMapper studentCourseMapper;

    @Override
    public String getMyCourse(BigInteger studentId) {
        return studentCourseMapper.getMyCourse(studentId);
    }

    @Override
    public Boolean addCourse(BigInteger studentId, String stuCourses) {
        return studentCourseMapper.addCourse(studentId, stuCourses);
    }
}
