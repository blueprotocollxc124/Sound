package org.linkworld.yuansystem.controller;

/*
 *@Author  LXC BlueProtocol
 *@Since   2022/2/28
 */


import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class TeacheraseController {
 @Autowired
 protected HttpServletRequest request;

 @Autowired
 protected HttpServletResponse response;


 protected String getTeacherId() {
  HttpSession session = request.getSession();
  String studentId = session.getAttribute("teacherId").toString();
  return studentId;
 }
}
