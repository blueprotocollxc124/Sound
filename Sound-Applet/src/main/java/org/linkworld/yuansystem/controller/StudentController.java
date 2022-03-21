package org.linkworld.yuansystem.controller;

/*
 *@Author  LXC BlueProtocol
 *@Since   2022/2/27
 */


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.linkworld.yuansystem.check.sequence.LoginCheckSequence;
import org.linkworld.yuansystem.check.sequence.RegisterCheckSequence;
import org.linkworld.yuansystem.model.dto.*;
import org.linkworld.yuansystem.model.entity.Student;
import org.linkworld.yuansystem.model.vo.ResultBean;
import org.linkworld.yuansystem.properties.WeiXinProperties;
import org.linkworld.yuansystem.runner.SoundWeiXinRunner;
import org.linkworld.yuansystem.service.StudentService;
import org.linkworld.yuansystem.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.*;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Controller
@Slf4j
@RequestMapping("/applet/student")
@Api(tags = "与学生操作相关的控制器", description = "包含学生登录，注册等功能")
public class StudentController extends BaseController{

 @Autowired
 private StudentService studentService;

 @Autowired
 private RedisUtil redisUtil;

 @Autowired
 private Environment environment;

 @Autowired
 private RestTemplate restTemplate;

 @Autowired
 private IdUtil idUtil;





 /**
  * @Description: 学生登录接口
  * @date: 2022/2/28 19:25
  * @Param: [loginDTO]
  * @return: org.linkworld.yuansystem.model.vo.ResultBean
 */
 @PostMapping("/login")
 @ResponseBody
 @ApiOperation(value = "学生登录的接口")  // 0
 @ApiImplicitParams(@ApiImplicitParam(name = "loginDTO", value = "loginDTO对象", required = true))
 public ResultBean login(@RequestBody @Validated({LoginCheckSequence.class}) StudentLoginDTO loginDTO) {
  String url = "https://api.weixin.qq.com/sns/jscode2session?appid={appid}&secret={secret}&js_code={code}&grant_type=authorization_code";
  Map<String, String> requestMap = new HashMap<>();
  requestMap.put("appid", "wxe626ab68965d47b3");
  requestMap.put("secret","5c20d5cfd591676c1a0a06b4e52ccdb1");
  requestMap.put("code", loginDTO.getCode());
  ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class,requestMap);
  JSONObject jsonObject= JSONObject.parseObject(responseEntity.getBody());
  String openId=jsonObject.getString("openid");
  String session_key=jsonObject.getString("session_key");
  JWTTokenUtil jwtTokenUtil = new JWTTokenUtil();
  String jwtToken = jwtTokenUtil.generateToken(openId, session_key);
  ResultBean resultBean = studentService.studentLogin(loginDTO,openId);
  return resultBean.setData(jwtToken);
 }

 @GetMapping("/loginOut") // 0
 @ResponseBody
 @ApiOperation(value = "学生注销的接口",notes = "head加token")
 public ResultBean loginOut() {
  String token = request.getHeader("token");
  JWTTokenUtil jwtTokenUtil = new JWTTokenUtil();
  String openId = jwtTokenUtil.getOpenIdFromToken(token);
  LambdaQueryWrapper<Student> wrapper = new QueryWrapper<Student>().lambda().eq(Student::getOpenId, openId);
  Student student = studentService.getOne(wrapper);
  BigInteger studentIdBigInteger =student.getStuId();
  String studentId = studentIdBigInteger.toString();
  student.setOpenId("");
  studentService.updateById(student);
  redisUtil.del(studentId);
  //request.getSession().invalidate();
  return ResultBean.ok();
 }

 /**
  * @Description: 学生注册接口
  * @date: 2022/2/28 19:26
  * @Param: [registerDTO]
  * @return: org.linkworld.yuansystem.model.vo.ResultBean
 */
 @PostMapping("/register")
 @ResponseBody
 @ApiOperation(value = "学生注册的接口")  // 0
 @ApiImplicitParams(@ApiImplicitParam(name = "registerDTO", value = "registerDTO对象", required = true))
 public ResultBean register(@RequestBody @Validated({RegisterCheckSequence.class}) StudentRegisterDTO registerDTO) {
  LambdaQueryWrapper<Student> wrapper = new QueryWrapper<Student>().lambda().eq(Student::getPhone, registerDTO.getPhone());
  Student isExistsStudent = studentService.getOne(wrapper);
  if(isExistsStudent==null){
   return studentService.studentRegister(registerDTO);
  }
  return ResultBean.bad().setMessage("用户已经存在");
 }


 /**
  * @Description: 学生上传头像接口
  * @date: 2022/2/28 19:26
  * @Param: [headDTO]
  * @return: org.linkworld.yuansystem.model.vo.ResultBean
 */
 @PostMapping("/uploadHead")
 @ResponseBody
 @ApiOperation(value = "上传用户头像的接口",notes = "head加token")// 0
 @ApiImplicitParams(@ApiImplicitParam(name = "file", value = "CommonsMultipartFile对象", required = true))
 public ResultBean upLoadHeadAddr(@RequestParam("file") MultipartFile file) throws IOException {
  String token = request.getHeader("token");
  BigInteger studentIdBigInteger = idUtil.getStudentId(token);
  String studentId = studentIdBigInteger.toString();
  String headAddr = upLoad(file, studentId);
  String studentInfo = "studentInfo";
  Map<Object, Object> studentMap = redisUtil.hmget(studentId);
  Student student = JsonUtil.string2Obj(studentMap.get(studentInfo).toString(), Student.class);
  student.setHeadAddr(headAddr);
  studentMap.replace("studentInfo",student);
  studentService.updateById(student);
  redisUtil.hmset(studentId, MapUtil.objectToStringMap(studentMap));
  return ResultBean.ok().setData(headAddr);
 }


 @GetMapping("/getStudentInfo")
 @ResponseBody
 @ApiOperation(value = "查看学生个人信息的接口",notes="head加token")  // 0
 public ResultBean seeStudentInfo() {
  String token = request.getHeader("token");
  BigInteger studentIdBigInteger = idUtil.getStudentId(token);
  String studentId = studentIdBigInteger.toString();
  Map<Object, Object> studentIdMap = redisUtil.hmget(studentId);
  Object studentInfo = studentIdMap.get("studentInfo");
  Student student = null;
  student = JsonUtil.string2Obj(studentInfo.toString(), Student.class);
  if(student==null) {
   student = studentService.getById(studentId);
   studentIdMap.put("studentIdInfo",student);
   redisUtil.hmset(studentId, MapUtil.objectToStringMap(studentIdMap));
  }
  return ResultBean.ok().setData(new StudentInfoDTO(student));
 }


 @PostMapping("/editStudentInfo")
 @ResponseBody
 @ApiOperation(value = "编辑学生个人资料的接口",notes = "head加token")  // 0
 @ApiImplicitParams(@ApiImplicitParam(name = "EditStudentInfoDTO对象",required = true))
 public ResultBean editStudentId(@RequestBody  EditInfo dto) {
  String token = request.getHeader("token");
  BigInteger studentIdBigInteger = idUtil.getStudentId(token);
  String studentId = studentIdBigInteger.toString();
  Map<Object, Object> studentIdMap = redisUtil.hmget(studentId);
  Object studentInfo = studentIdMap.get("studentInfo");
  Student student = JsonUtil.string2Obj(studentInfo.toString(), Student.class);
  student.setName(dto.getName()).setSignature(dto.getSignature()).setSex(dto.getSex());
  studentIdMap.replace("studentInfo",student);
  redisUtil.hmset(studentId,MapUtil.objectToStringMap(studentIdMap));
  studentService.updateById(student);
  return ResultBean.ok();
 }

 @PostMapping("/isExistPhone")
 @ResponseBody
 @ApiOperation(value = "判断手机号码是否存在接口")
 public ResultBean isExistPhone(String phone) {
  System.out.println("手机号码---->"+phone);
  LambdaQueryWrapper<Student> wrapper = new QueryWrapper<Student>().lambda().eq(Student::getPhone, phone);
  Student student = studentService.getOne(wrapper);
  if(student==null) {
   return ResultBean.ok().setData(false);
  }
  return ResultBean.ok().setData(true);
 }




 /*
  * @Description: 学生上传头像的方法
  * @date: 2022/2/28 19:26
  * @Param: [fileAddress, studentId]
  * @return: java.lang.String
 */
 public String upLoad(MultipartFile file,String studentId) throws IOException {
  InputStream fileInputStream = file.getInputStream();
  Profiles of = Profiles.of("dev");
  boolean isDev = environment.acceptsProfiles(of);
  String headAddr = "";
  if(isDev) {
    headAddr = "D:\\IDEA__project\\Backend\\Sound-System\\src\\main\\resources\\static\\heads\\" + studentId + ".jpg";
  }else {
    headAddr = File.separator+ "opt"+File.separator + "jar"+File.separator+"heads" +File.separator+ studentId + ".jpg";
  }
  FileOutputStream fileOutputStream = new FileOutputStream(new File(headAddr));
  int len = 0;
  byte[] bytes = new byte[1024];
  while ((len=fileInputStream.read(bytes))!=-1) {
   fileOutputStream.write(bytes,0,len);
  }
  if(len==-1) {
   log.info(AnsiOutput.toString(AnsiColor.BRIGHT_BLUE,"用户ID："+studentId+"，头像上传成功！"));
  }
  return headAddr;
 }








}
