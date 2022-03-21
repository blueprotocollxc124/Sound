package org.linkworld.yuansystem.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.linkworld.yuansystem.dao.StudentCourseMapper;
import org.linkworld.yuansystem.entity.CourseTeacher;
import org.linkworld.yuansystem.exception.NotSuchObjectException;
import org.linkworld.yuansystem.model.dto.StudentClassDTO;
import org.linkworld.yuansystem.model.entity.Course;
import org.linkworld.yuansystem.model.entity.Student;
import org.linkworld.yuansystem.model.entity.Teacher;
import org.linkworld.yuansystem.model.vo.CourseVO;
import org.linkworld.yuansystem.model.vo.ResultBean;
import org.linkworld.yuansystem.service.*;
import org.linkworld.yuansystem.util.IdUtil;
import org.linkworld.yuansystem.util.JWTTokenUtil;
import org.linkworld.yuansystem.util.MapUtil;
import org.linkworld.yuansystem.util.RedisUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


/*
 *@Author  LXC BlueProtocol
 *@Since   2022/2/15
 */




@Controller
@Api(tags = "课程控制器",description = "包含我的课程列表，添加课程，课程详细和学生最近查看课程等")
@RequestMapping("/applet/course")
public class CourseController extends BaseController implements ApplicationContextAware {

    @Autowired
    StudentService studentService;

    @Autowired
    CourseService courseService;

    @Autowired
    CourseTeacherService courseTeacherService;

    @Autowired
    TeacherService teacherService;

    @Autowired
    StudentCourseService studentCourseService;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    IdUtil idUtil;



    private ApplicationContext applicationContext;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * @Description: 我的课程列表
     * @date: 2022/2/17 10:34
     * @Param: [studentId]
     * @return: org.linkworld.yuansystem.model.vo.ResultBean
    */
    @GetMapping("/myCourseList")  // 0
    @ResponseBody
    @ApiOperation(value = "我的课程列表接口",notes = "head加token")
    public ResultBean myCourseList() {
        String token = request.getHeader("token");
        BigInteger studentId = idUtil.getStudentId(token);
        String myCourses = studentCourseService.getMyCourse(studentId);
        String[] courses = myCourses.split(",");
        if(courses.length == 0) {
            return ResultBean.ok().setMessage("您还没有加入任何课程");
        }
        ArrayList<CourseVO> courseList = new ArrayList<>();
        for (String course : courses) {
            System.out.println(course);
            Course realCourse = courseService.getById(new BigInteger(course));
            System.out.println(realCourse);
            CourseVO courseVo = getCourseVo(realCourse);
            courseList.add(courseVo);
        }
        return ResultBean.ok().setData(courseList);

    }
    /**
     * @Description: 添加课程到我的课程中
     * @date: 2022/2/17 18:20
     * @Param: [studentAddCourseDTO]
     * @return: org.linkworld.yuansystem.model.vo.ResultBean
    */
    @GetMapping("/addToMyCourse/{courseId}")
    @ResponseBody
    @ApiOperation(value = "添加课程接口",notes = "head加token")  // 0
    @ApiImplicitParams(@ApiImplicitParam(name = "courseId",value = "课程id",required = true))
    public ResultBean addToMyCourse(@PathVariable("courseId") BigInteger courseId) {
        String token = request.getHeader("token");
        BigInteger studentId = idUtil.getStudentId(token);
        String myCourses = studentCourseService.getMyCourse(studentId);
        if(!StringUtils.hasText(myCourses)) {
            String addedMyCourse = myCourses.concat(courseId.toString());
            studentCourseService.addCourse(studentId,addedMyCourse);
            return ResultBean.ok();
        }
        String addedMyCourse = myCourses.concat(","+courseId.toString());
        studentCourseService.addCourse(studentId,addedMyCourse);
        return ResultBean.ok();
    }


    /**
     * @Description: 课程详细，未完善 0
     * @date: 2022/2/17 10:34
     * @Param: [courseDetailDTO]
     * @return: org.linkworld.yuansystem.model.vo.ResultBean
    */
    @GetMapping("/courseDetail/{courseId}")
    @ResponseBody
    @ApiOperation(value = "课程详细接口，未完善",notes = "head加token")
    @ApiImplicitParams({@ApiImplicitParam(name = "courseId",value = "课程id",required = true)}
    )
    public ResultBean courseDetail(@PathVariable("courseId")BigInteger courseId) {
        String token = request.getHeader("token");
        BigInteger studentIdBigInteger = idUtil.getStudentId(token);
        String studentId = studentIdBigInteger.toString();
        Course course = Optional.ofNullable(courseService.getById(courseId))
                .orElseThrow(() -> new NotSuchObjectException("Course对象为空"));
        CourseVO courseVo = getCourseVo(course);
        Map<Object, Object> studentIdMap = redisUtil.hmget(studentId);
        boolean hasRecentlyCourse = redisUtil.hasHasKey(studentId, "recentlyCourse");
        if(hasRecentlyCourse==false) {
            ArrayList<CourseVO> recentlyInitList = new ArrayList<CourseVO>();
            studentIdMap.put("recentlyCourse",recentlyInitList);
            Map<String, Object> studentIdInitRecentlyCourseMap = MapUtil.objectToStringMap(studentIdMap);
            redisUtil.hmset(studentId,studentIdInitRecentlyCourseMap);

        }

            boolean hasRecentlyCourseCount = redisUtil.hasHasKey(studentId, "recentlyCourseCount");
            if(hasRecentlyCourseCount==false) {
                int CourseCount = 0;
                studentIdMap.put("recentlyCourseCount", CourseCount);
                Map<String, Object> studentIdMapAddCount = MapUtil.objectToStringMap(studentIdMap);
                redisUtil.hmset(studentId, studentIdMapAddCount);
            }
            int recentlyCourseCount = (int) studentIdMap.get("recentlyCourseCount");
        List<CourseVO> studentRecentlyCourseList = JSON.parseArray(studentIdMap.get("recentlyCourse").toString(), CourseVO.class);
                if (studentRecentlyCourseList.size() != 2) {
                    System.out.println(studentRecentlyCourseList.size());
                    if(studentRecentlyCourseList.size()==0) {
                        studentRecentlyCourseList.add(courseVo);
                    } else {
                    int distinct =0;
                    for (CourseVO courseVO : studentRecentlyCourseList) {
                        if(courseVO.getCourseId().equals(courseVo.getCourseId())) {
                            distinct++;
                        }
                    }
                    if(distinct==0) {
                        studentRecentlyCourseList.add(courseVo);
                    }
                    }
            }
            else {
                    int distinct = 0;
                    for (CourseVO courseVO : studentRecentlyCourseList) {
                        if(courseVO.getCourseId().equals(courseVo.getCourseId()) ) {
                            distinct++;
                        }
                    }
                    if(distinct==0) {
                        studentRecentlyCourseList.set(recentlyCourseCount++, courseVo);
                        if (recentlyCourseCount == 2) {
                            recentlyCourseCount = 0;
                        }
                    }


            }

            studentIdMap.replace("recentlyCourse",studentRecentlyCourseList);
            studentIdMap.replace("recentlyCourseCount",recentlyCourseCount);
            Map<String, Object> studentIdMapStr = MapUtil.objectToStringMap(studentIdMap);
            redisUtil.hmset(studentId,studentIdMapStr);

        return ResultBean.ok().setData(courseVo);
    }


    /**
     * @Description: 获取学生最近查看课程接口 0
     * @date: 2022/2/17 11:26
     * @Param: [studentId]
     * @return: org.linkworld.yuansystem.model.vo.ResultBean
    */
    @GetMapping("/recentlyCourse")
    @ResponseBody
    @ApiOperation(value = "获取学生最近查看课程借口",notes = "head加token,data中存放最近课程List集合")
    @ApiImplicitParam(name = "studentId",value = "学生id",required = true)
    public ResultBean recentlyTwoCourse() {
        String token = request.getHeader("token");
        BigInteger studentIdBigInteger = idUtil.getStudentId(token);
        String studentIdStr = studentIdBigInteger.toString();
        List<CourseVO> recentlyCourseVoList = (List<CourseVO>) redisUtil.hmget(studentIdStr).get("recentlyCourse");
        if(recentlyCourseVoList==null) {
            return ResultBean.ok().setMessage("您还没有查看任何课程");
        }
        return ResultBean.ok().setData(recentlyCourseVoList);
    }


    /**
     * @Description: 获取CourseVo对象
     * @date: 2022/2/16 23:48
     * @Param: [course]
     * @return: org.linkworld.yuansystem.model.vo.CourseVO
    */
    private CourseVO getCourseVo(Course course) {
        LambdaQueryWrapper<CourseTeacher> wrapper =
                new QueryWrapper<CourseTeacher>().lambda().eq(CourseTeacher::getCourseId, course.getCourseId());
        CourseTeacher courseTeacher = Optional
                .ofNullable(courseTeacherService.getOne(wrapper))
                .orElseThrow(() -> new NotSuchObjectException("没有这样的CourseTeacher对象"));
        System.out.println(courseTeacher);
        Teacher teacher = teacherService.getById(courseTeacher.getTeacherId());
        CourseVO courseVO = new CourseVO()
                .setCourseId(course.getCourseId().toString())
                .setCourseName(course.getCourseName())
                .setCourseTeacher(teacher.getTeachername())
                .setHeadAddr(course.getHeadAddr());
        return courseVO;
    }


}

