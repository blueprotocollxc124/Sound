package org.linkworld.yuansystem.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.linkworld.yuansystem.model.dto.TeacherEmailSendDTO;
import org.linkworld.yuansystem.model.entity.Email;
import org.linkworld.yuansystem.entity.StudentEmail;
import org.linkworld.yuansystem.model.dto.SendEmailDTO;
import org.linkworld.yuansystem.model.entity.Course;
import org.linkworld.yuansystem.model.vo.ResultBean;
import org.linkworld.yuansystem.service.CourseService;
import org.linkworld.yuansystem.service.EmailService;
import org.linkworld.yuansystem.service.StudentEmailService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.validation.Valid;
import java.io.*;
import java.math.BigInteger;

/**
 * @author BlueProtocol
 * @since 2022-03-03
 */
@Controller
@Slf4j
@RequestMapping("/system")
public class EmailController extends TeacheraseController {

    @Autowired
    private Environment environment;

    @Autowired
    private CourseService courseService;

    @Autowired
    private StudentEmailService studentEmailService;

    @Autowired
    private EmailService emailService;


    @PostMapping("/writeEmail")  // 该接口等写完教师登录之后完善 0
    @ResponseBody
    @ApiOperation(value = "教师写邮件的接口")
    public ResultBean writeEmail(@RequestBody @Valid TeacherEmailSendDTO dto) {
        //String teacherIdStr = getTeacherId();
        String teacherIdStr = "210";
        BigInteger teacherId = new BigInteger(teacherIdStr);
        Email email = new Email().setTeacherId(teacherId).setTitle(dto.getTitle()).setContent(dto.getContent());
        emailService.save(email);
        return ResultBean.ok();
    }

    @PostMapping("/sendEmails")  // 0
    @ResponseBody
    @ApiOperation(value = "教师发送邮件给班级学生的接口", tags = "SendEmailDTO里面传入课程id和email对象")
    public ResultBean sendEmailToStudent(@RequestBody @Valid SendEmailDTO dto) {
        LambdaQueryWrapper<Course> wrapper = new QueryWrapper<Course>().lambda().eq(Course::getCourseId, dto.getCourseId());
        Course course = courseService.getOne(wrapper);
        String students = course.getCourseStudent();
        String[] studentIdStrs = students.split(",");
        for (String studentIdStr : studentIdStrs) {
            boolean save = studentEmailService.save(new StudentEmail()
                    .setStuId(new BigInteger(studentIdStr))
                    .setEmailId(dto.getEmail().getEmailId()));
        }
        return ResultBean.ok();
    }


    /**    0
     * @Description: 教师上传邮件图片的方法
     * @date: 2022/2/28 19:26
     * @Param: [fileAddress, studentId]
     * @return: java.lang.String
     */
    public String upLoad(MultipartFile file, String emailId) throws IOException {
        InputStream fileInputStream = file.getInputStream();
        Profiles of = Profiles.of("dev");
        boolean isDev = environment.acceptsProfiles(of);
        String headAddr = "";
        if(isDev) {
            headAddr = "D:\\IDEA__project\\Backend\\Sound-System\\src\\main\\resources\\static\\emailPicture\\" + emailId + ".jpg";
        }else {
            headAddr = File.separator+"opt"+File.separator+"jar"+File.separator+"emailPicture"+File.separator+ emailId + ".jpg";
        }
        FileOutputStream fileOutputStream = new FileOutputStream(new File(headAddr));
        int len = 0;
        byte[] bytes = new byte[1024];
        while ((len=fileInputStream.read(bytes))!=-1) {
            fileOutputStream.write(bytes,0,len);
        }
        if(len==-1) {
            log.info(AnsiOutput.toString(AnsiColor.BRIGHT_BLUE,"邮件ID："+emailId+"，邮箱图片上传成功！"));
        }
        return headAddr;
    }
}

