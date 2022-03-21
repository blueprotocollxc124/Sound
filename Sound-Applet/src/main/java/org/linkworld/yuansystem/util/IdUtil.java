package org.linkworld.yuansystem.util;

/*
 *@Author  LXC BlueProtocol
 *@Since   2022/3/19
 */


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.linkworld.yuansystem.model.entity.Student;
import org.linkworld.yuansystem.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component
public class IdUtil {

 @Autowired
 private  StudentService studentService;

 public   BigInteger getStudentId(String token) {
  JWTTokenUtil jwtTokenUtil = new JWTTokenUtil();
  String openId = jwtTokenUtil.getOpenIdFromToken(token);
  LambdaQueryWrapper<Student> wrapper = new QueryWrapper<Student>().lambda().eq(Student::getOpenId, openId);
  Student student = studentService.getOne(wrapper);
  BigInteger studentIdBigInteger = student.getStuId();
  return studentIdBigInteger;
 }

}
