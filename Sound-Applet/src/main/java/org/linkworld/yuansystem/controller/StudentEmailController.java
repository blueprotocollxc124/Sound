package org.linkworld.yuansystem.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.linkworld.yuansystem.entity.StudentEmail;
import org.linkworld.yuansystem.exception.NotSuchObjectException;
import org.linkworld.yuansystem.model.entity.Email;
import org.linkworld.yuansystem.model.entity.Student;
import org.linkworld.yuansystem.model.vo.CourseVO;
import org.linkworld.yuansystem.model.vo.EmailVO;
import org.linkworld.yuansystem.model.vo.ResultBean;
import org.linkworld.yuansystem.service.EmailService;
import org.linkworld.yuansystem.service.StudentEmailService;
import org.linkworld.yuansystem.service.StudentService;
import org.linkworld.yuansystem.util.IdUtil;
import org.linkworld.yuansystem.util.JWTTokenUtil;
import org.linkworld.yuansystem.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.core.env.Profiles;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author BlueProtocol
 * @since 2022-03-03
 */
@Controller
@RequestMapping("/applet/studentEmail")
@Api(tags = "与学生邮件相关的控制器", description = "包含首页的邮件模块、搜索邮件等")
public class StudentEmailController extends BaseController{

    @Autowired
    private StudentEmailService studentEmailService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private IdUtil idUtil;


    @PostMapping("/searchEmail")  // 0
    @ResponseBody
    @ApiOperation(value = "消息模块中搜索的接口",notes = "head加token，data中存有根据title查出来的邮件")
    @ApiImplicitParams(@ApiImplicitParam(name = "searchContent", value = "搜索内容"))
    public ResultBean studentSearchEmailOrRemind(@Param("searchContent") String searchContent) {
        String token = request.getHeader("token");
        BigInteger studentIdBigInteger = idUtil.getStudentId(token);
        String studentId = studentIdBigInteger.toString();

        LambdaQueryWrapper<StudentEmail> wrapper = new QueryWrapper<StudentEmail>().lambda().eq(StudentEmail::getStuId, studentId);
        List<StudentEmail> studentEmailList = studentEmailService.list(wrapper);
        ArrayList<EmailVO> studentEmailVoList = new ArrayList<>();
        for (StudentEmail studentEmail : studentEmailList) {
            Email email = emailService.getById(studentEmail.getEmailId());
            studentEmailVoList.add(new EmailVO(email,studentEmail.getStatue()));
        }
        List<EmailVO> searchEmailVoList = studentEmailVoList.stream().filter(emailVO -> {
            return emailVO.getTitle().contains(searchContent);
        }).collect(Collectors.toList());
        return ResultBean.ok().setData(searchEmailVoList);
    }

    @GetMapping("/toMessageModule")  // 0
    @ResponseBody
    @ApiOperation(value = "学生点击消息模块的接口", notes = "head加token，data里面存map，" +
            "map中存上课提醒的列表'remindStudentList(时间已排序)',邮箱列表'emailList'(时间已排序),未读的邮箱数量unReadEmailCount," +
            "未读的上课提醒数量unReadRemindCount" +
            "上课提醒和邮箱包含图片路径，标题，内容，状态，时间，邮箱/教师id，状态0表示未读")
    public ResultBean messagePage() {
        String token = request.getHeader("token");
        BigInteger studentIdBigInteger = idUtil.getStudentId(token);
        String studentId = studentIdBigInteger.toString();
        LambdaQueryWrapper<StudentEmail> wrapper = new QueryWrapper<StudentEmail>().lambda().eq(StudentEmail::getStuId, studentId);
        List<StudentEmail> studentEmailList = studentEmailService.list(wrapper);
        ArrayList<EmailVO> studentDetailEmailVolList = new ArrayList();
        studentEmailList.forEach(studentEmail -> {
            Email email = emailService.getById(studentEmail.getEmailId());
            studentDetailEmailVolList.add(new EmailVO(email,studentEmail.getStatue()));
        });

        List<EmailVO> remindStudentList = studentDetailEmailVolList.stream().filter(emailVO -> emailVO.getTitle().contains("》上课提醒")).collect(Collectors.toList());
        List<EmailVO> sortedRemindStudentList = remindStudentList.stream().sorted((remind1, remind2) -> {
            return (remind1.getGmtCreate().getTime() - remind2.getGmtCreate().getTime()) > 0 ? 1 : -1;
        }).collect(Collectors.toList());
        List<EmailVO> emailList = studentDetailEmailVolList.stream().filter(emailVO -> !emailVO.getTitle().contains("》上课提醒")).collect(Collectors.toList());
        List<EmailVO> sortedEmailList = emailList.stream().sorted((email1, email2) -> {
            return (email1.getGmtCreate().getTime() - email2.getGmtCreate().getTime()) > 0 ? 1 : -1;
        }).collect(Collectors.toList());
        int unReadEmailCount = emailList.stream().filter(emailVO -> {
            return emailVO.getStatus().equals(0);
        }).collect(Collectors.toList()).size();
        int unReadRemindCount = remindStudentList.stream().filter((emailVO -> {
            return emailVO.getStatus().equals(0);
        })).collect(Collectors.toList()).size();
        HashMap<String, Object> map = new HashMap<>();
        map.put("remindStudentList",sortedRemindStudentList);
        map.put("emailList",sortedEmailList);
        map.put("unReadEmailCount",unReadEmailCount);
        map.put("unReadRemindCount",unReadRemindCount);
        return ResultBean.ok().setData(map);
    }

    @PostMapping("/toSeeEmail")  // 0
    @ResponseBody
    @ApiOperation(value = "点击收件箱的接口")
    @ApiImplicitParams(@ApiImplicitParam(name = "emailVO",value = "将传到消息页面的emailList集合传进来即可"))
    public ResultBean EmailPage(@RequestBody List<EmailVO> emilList) {
        Assert.notNull(emilList,"传入的邮箱列表不能为空");
        return ResultBean.ok().setData(emilList);

    }


    @PostMapping("/getEmailDetail")  // 0
    @ResponseBody
    @ApiOperation(value = "查看邮箱和上课提醒详细的接口", notes = "data里面返回排序完之后的邮箱集合，包含图片路径，标题，内容，状态，时间，邮箱/教师id")
    @ApiImplicitParams(@ApiImplicitParam(name = "emailVO",value = "将点击的emailVo对象传入"))
    public ResultBean emailDetail(@RequestBody EmailVO emailVO) {
        EmailVO emailVoOption = Optional.ofNullable(emailVO).orElseThrow(() -> {
            return new NotSuchObjectException("传入的emailVO不能为空");
        });
        return ResultBean.ok().setData(emailVoOption);
    }

    @PostMapping("/alreadyRead")  // 0
    @ResponseBody
    @ApiOperation(value = "学生点击邮箱和上课提醒详细已读接口",notes = "head加token")
    @ApiImplicitParams(@ApiImplicitParam(name = "emailVO",value = "将点击的emailVo对象传入"))
    public ResultBean oneRead(@RequestBody EmailVO emailVO) {
        String token = request.getHeader("token");
        BigInteger studentIdBigInteger = idUtil.getStudentId(token);
        String studentId = studentIdBigInteger.toString();
        LambdaQueryWrapper<StudentEmail> wrapper = new QueryWrapper<StudentEmail>().lambda().eq(StudentEmail::getStuId, studentId).eq(StudentEmail::getEmailId, emailVO.getEmailId());
        StudentEmail studentEmail = studentEmailService.getOne(wrapper);
        studentEmail.setStatue(1);
        boolean isSucceed = studentEmailService.update(studentEmail, wrapper);
        return ResultBean.ok().setSuccess(isSucceed);
    }


    @GetMapping("/allEmailRead")// 0
    @ResponseBody
    @ApiOperation(value = "全部已读接口",notes = "head加token")
    public ResultBean allEmailRead() {
        String token = request.getHeader("token");
        BigInteger studentIdBigInteger = idUtil.getStudentId(token);
        String studentId = studentIdBigInteger.toString();
        LambdaQueryWrapper<StudentEmail> wrapper = new QueryWrapper<StudentEmail>().lambda().eq(StudentEmail::getStuId, studentId);
        List<StudentEmail> studentEmailList = studentEmailService.list(wrapper);
        System.out.println(studentEmailList);
        studentEmailList.forEach(studentEmail -> {
            studentEmail.setStatue(1);
            studentEmailService.update(studentEmail,wrapper);
        });
        return ResultBean.ok();
    }

    @PostMapping("/partsEmailRead")// 0
    @ResponseBody
    @ApiOperation(value = "批量标记邮件和上课提醒已读接口",notes = "head加token")
    @ApiImplicitParams(@ApiImplicitParam(name = "emailList",value = "将批量编辑的email添加到list集合中传入"))
    public ResultBean selectedEmailRead(@RequestBody List<EmailVO> emailList) {
        Assert.notNull(emailList,"Email集合不能null");
        String token = request.getHeader("token");
        BigInteger studentId = idUtil.getStudentId(token);
        for (EmailVO email : emailList) {
            StudentEmail studentEmail = studentEmailService.getSelectedEmail(studentId,email.getEmailId());
            studentEmail.setStatue(1);
            LambdaUpdateWrapper<StudentEmail> wrapper = new UpdateWrapper<StudentEmail>().lambda().eq(StudentEmail::getEmailId, email.getEmailId()).eq(StudentEmail::getStuId,studentId);
            studentEmailService.update(studentEmail,wrapper);
        }
       return ResultBean.ok();
    }

    @PostMapping("/partsEmailDelete")// 0
    @ResponseBody
    @ApiOperation(value = "批量删除邮件接口",notes = "head加token")
    @ApiImplicitParams(@ApiImplicitParam(name = "emailList",value = "将批量编辑的email添加到list集合中传入"))
    public ResultBean selectedEmailDelete(@RequestBody List<EmailVO> emailList) {
        Assert.notNull(emailList,"Email集合不能null");
        String token = request.getHeader("token");
        BigInteger studentId = idUtil.getStudentId(token);
        for (EmailVO email : emailList) {
           studentEmailService.deleteByEmail(studentId,email.getEmailId());
        }
       return ResultBean.ok();
    }







}

