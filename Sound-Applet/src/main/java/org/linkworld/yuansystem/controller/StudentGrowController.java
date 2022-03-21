package org.linkworld.yuansystem.controller;

/*
 *@Author  LXC BlueProtocol
 *@Since   2022/3/4
 */

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.linkworld.yuansystem.model.dto.RecordDTO;
import org.linkworld.yuansystem.model.dto.RecordExecDTO;
import org.linkworld.yuansystem.model.entity.Course;
import org.linkworld.yuansystem.model.entity.Student;
import org.linkworld.yuansystem.model.entity.StudentCourse;
import org.linkworld.yuansystem.model.vo.GroupCourseVO;
import org.linkworld.yuansystem.model.vo.ResultBean;
import org.linkworld.yuansystem.properties.FileProperties;
import org.linkworld.yuansystem.service.CourseService;
import org.linkworld.yuansystem.service.StudentCourseService;
import org.linkworld.yuansystem.service.StudentService;
import org.linkworld.yuansystem.util.IdUtil;
import org.linkworld.yuansystem.util.JWTTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/applet/studentGrow")
@Api(tags = "与学生成长记录相关的控制器")
public class StudentGrowController extends BaseController {

    @Autowired
    private StudentCourseService studentCourseService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private IdUtil idUtil;


    @GetMapping("/myCourseList")   // 0
    @ResponseBody
    @ApiOperation(value = "学生成长记录页面的接口",
            notes = "head加token,data里面存着学生参加课程的集合，集合里面的GroupCourseVO包含courseId和courseName")
    public ResultBean toStudentGrowPage() {
        String token = request.getHeader("token");
        BigInteger studentIdBigInteger = idUtil.getStudentId(token);
        String studentId = studentIdBigInteger.toString();
        LambdaQueryWrapper<StudentCourse> wrapper = new QueryWrapper<StudentCourse>().lambda().eq(StudentCourse::getStuId, studentId);
        StudentCourse studentCourse = studentCourseService.getOne(wrapper);
        String courses = studentCourse.getStuCourses();
        String[] myCourseIds = courses.split(",");
        ArrayList<GroupCourseVO> courseList = new ArrayList<>();
        for (String courseIdStr : myCourseIds) {
            LambdaQueryWrapper<Course> courseWrapper = new QueryWrapper<Course>().lambda().eq(Course::getCourseId, courseIdStr);
            courseList.add(new GroupCourseVO(courseService.getOne(courseWrapper)));
        }
        return ResultBean.ok().setData(courseList);
    }



    @PostMapping("/toOneCourseRecord")  // 0
    @ResponseBody
    @ApiOperation(value = "进入《xx》课程录音的接口",
            notes = "head加token,data里面存returnMap，returnMap中存有workIdMap和workExecIdMap，" +
                    "workIdMap的大小就是该课程有多少个作业，作业id从1开始，形式是workIdMap workId workList，而里面是file对象" +
                    "练习的和作业是一样的逻辑，这个排序就交给你们了")
    @ApiImplicitParams(@ApiImplicitParam(name = "GroupCourseVO",required = true))
    public ResultBean toOneCourseRecord(@RequestBody @Valid GroupCourseVO vo) {
        String token = request.getHeader("token");
        BigInteger studentIdBigInteger = idUtil.getStudentId(token);
        String studentId = studentIdBigInteger.toString();
        File studentWorks = new File(FileProperties.STUDENT_WORK_PATH);
        File[] files = studentWorks.listFiles();
        ArrayList<RecordDTO> studentCourseWorkList = new ArrayList();
        ArrayList<RecordExecDTO> studentCourseExecList = new ArrayList<>();
        for (File file : files) {
            if(file.getName().equals(studentId)) {
                // 学生作业目录，练习也在里面，但是练习有个包
                for (File studentIdCourseFile : file.listFiles()) {
                    if(studentIdCourseFile.getName().contains(vo.getCourseId().toString())) {
                        String workFileName = studentIdCourseFile.getName();
                        int workFirstOne = workFileName.indexOf("-");
                        int workLastOne = workFileName.lastIndexOf("-");
                        int workBigOne = workFileName.indexOf("—");
                        String workId = workFileName.substring(workFirstOne + 1, workLastOne);
                        String workCode = workFileName.substring(workLastOne + 1, workBigOne);
                        // 关于学生课程的文件
                        studentCourseWorkList.add(new RecordDTO().setWorkId(workId).setCode(workCode).setFile(studentIdCourseFile));
                        continue;
                    } // 练习放在exec包下
                    if(studentIdCourseFile.getName().equals("exec")) {
                        for (File execFile : studentIdCourseFile.listFiles()) {

                            String fileName = execFile.getName();
                            int firstOne = fileName.indexOf("-");
                            int lastOne = fileName.lastIndexOf("-");
                            int firstBigOne = fileName.indexOf("—");
                            String execId = fileName.substring(firstOne + 1, lastOne);
                            String code = fileName.substring(lastOne + 1, firstBigOne);
                            studentCourseExecList.add(new RecordExecDTO().setExecId(execId).setCode(code).setFile(execFile));

                        }
                    }

                }
            }
        }
        //
        // 作业List 作业id list集合   workIdMap workId workList
        //
        HashMap<String, Object> workIdMap = new HashMap<>();
        for (RecordDTO recordDTO : studentCourseWorkList) {
            int beforeWorkId = recordDTO.getFile().getName().indexOf("-");
            int afterWorkId = recordDTO.getFile().getName().lastIndexOf("-");
            String detailWorkNameFromId = recordDTO.getFile().getName().substring(beforeWorkId+1,afterWorkId);
            if(!workIdMap.containsKey(detailWorkNameFromId)) {
                ArrayList<RecordDTO> WorkIdSubList = new ArrayList<>();
                WorkIdSubList.add(recordDTO);
                workIdMap.put(detailWorkNameFromId,WorkIdSubList);
            }else {
               List<RecordDTO> fileList = (List<RecordDTO>) workIdMap.get(detailWorkNameFromId  );
               fileList.add(recordDTO);
               workIdMap.replace(detailWorkNameFromId,fileList);
            }
        }
        HashMap<String, Object> workExecIdMap = new HashMap<>();
        for (RecordExecDTO recordExecDTO : studentCourseExecList) {
            int beforeWorkId = recordExecDTO.getFile().getName().indexOf("-");
            int afterWorkId = recordExecDTO.getFile().getName().lastIndexOf("-");
            String detailWorkExecNameFromId = recordExecDTO.getFile().getName().substring(beforeWorkId+1,afterWorkId);
            if(!workExecIdMap.containsKey(detailWorkExecNameFromId)) {
                ArrayList<RecordExecDTO> WorkIdExecSubList = new ArrayList<>();
                WorkIdExecSubList.add(recordExecDTO);
                workExecIdMap.put(detailWorkExecNameFromId,WorkIdExecSubList);
            }else {
                List<RecordExecDTO> fileList = (List<RecordExecDTO>) workExecIdMap.get(detailWorkExecNameFromId);
                fileList.add(recordExecDTO);
                workExecIdMap.replace(detailWorkExecNameFromId,fileList);
            }
        }

        HashMap<String, Object> returnMap = new HashMap<>();
        returnMap.put("workIdMap",workIdMap);
        returnMap.put("workExecIdMap",workExecIdMap);
        return ResultBean.ok().setData(returnMap);
    }


}
