package org.linkworld.yuansystem.config;

/*
 *@Author  LXC BlueProtocol
 *@Since   2022/2/27
 */


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Hash;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.linkworld.yuansystem.model.entity.Student;
import org.linkworld.yuansystem.properties.WeiXinProperties;
import org.linkworld.yuansystem.service.StudentService;
import org.linkworld.yuansystem.util.MapUtil;
import org.linkworld.yuansystem.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class StudentRealm extends AuthorizingRealm {

 @Autowired
 private StudentService studentService;
 
 @Autowired
 private RedisUtil redisUtil;

 @Autowired
 private RestTemplate restTemplate;


 // 授权
 @Override
 protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
  SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
  Subject subject = SecurityUtils.getSubject();
  Student studentPrincipal = (Student) subject.getPrincipal();
  info.addStringPermission(studentPrincipal.getPerms());
  return info;
 }

 // 认证
 @Override
 protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
  UsernamePasswordToken token=(UsernamePasswordToken)authenticationToken;
  Subject subject = SecurityUtils.getSubject();
  LambdaQueryWrapper<Student> wrapper = new QueryWrapper<Student>().lambda()
          .eq(Student::getPhone, token.getUsername());
  Student student = studentService.getOne(wrapper);
  if(student==null) {
   return null;
  }
  String openId = token.getHost();
  student.setOpenId(openId);
  studentService.update(student,wrapper);

  String stuIdStr = student.getStuId().toString();
  if(!redisUtil.hasKey(stuIdStr)) {
   HashMap<String, Object> map = new HashMap<>();
   redisUtil.hmset(stuIdStr,map);
  }
  Map<Object, Object> studentMap = redisUtil.hmget(stuIdStr);
  if(studentMap.get("studentInfo")==null) {
   studentMap.put("studentInfo",student);
  }else{
   studentMap.replace("studentInfo",student);
  }
  redisUtil.hmset(stuIdStr, MapUtil.objectToStringMap(studentMap));
  Session session = subject.getSession();
  session.setAttribute("studentId",student.getStuId());
  return new SimpleAuthenticationInfo(student,student.getPassword(),getName());
 }

 @Override
 @Autowired
 public void setCredentialsMatcher(@Qualifier("MD5CredentialsMatcher") CredentialsMatcher credentialsMatcher) {
  super.setCredentialsMatcher(credentialsMatcher);
 }
}
