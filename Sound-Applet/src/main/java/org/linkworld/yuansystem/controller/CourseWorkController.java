package org.linkworld.yuansystem.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.linkworld.yuansystem.WorkStatueEnum;
import org.linkworld.yuansystem.entity.CourseWork;
import org.linkworld.yuansystem.entity.StudentWork;
import org.linkworld.yuansystem.exception.NotSuchObjectException;
import org.linkworld.yuansystem.model.dto.WorkDetailDTO;
import org.linkworld.yuansystem.model.entity.Student;
import org.linkworld.yuansystem.model.vo.ResultBean;
import org.linkworld.yuansystem.model.vo.WorkVo;
import org.linkworld.yuansystem.properties.FileProperties;
import org.linkworld.yuansystem.service.CourseService;
import org.linkworld.yuansystem.service.CourseWorkService;
import org.linkworld.yuansystem.service.StudentService;
import org.linkworld.yuansystem.service.StudentWorkService;
import org.linkworld.yuansystem.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.File;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author BlueProtocol
 * @since 2022-02-16
 */
@RestController
@Api(tags = "学生课程作业控制器",description = "包含作业列表，未完成作业详细内容等")
@RequestMapping("/applet/course")
public class CourseWorkController extends BaseController{

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private StudentService studentService;

    @Autowired
    private CourseWorkService courseWorkService;

    @Autowired
    private StudentWorkService studentWorkService;

    @Autowired
    private IdUtil idUtil;

    /**
     * @Description: 学生获取作业列表，已经排序完成 0 1
     * @date: 2022/2/16 22:25
     * @Param: [courseId] 
     * @return: ResultBean map()
    */
    @GetMapping("/workList/{courseId}")
    @ResponseBody
    @ApiOperation(value = "获取作业列表接口",notes = "head加token，集合中的studentWork对象包括stu_id,course_id,work_id,name,statue,score,teacher_comment,picture_path,start_time,end_time")
    @ApiImplicitParams(@ApiImplicitParam(name = "courseId",value = "课程id",required = true))
    public ResultBean getAllHomeWorkList(@PathVariable("courseId") BigInteger courseId) {
        Assert.notNull(courseId,"课程Id不能为空");
        String token = request.getHeader("token");
        BigInteger studentIdBigInteger = idUtil.getStudentId(token);
        String studentId = studentIdBigInteger.toString();
        LambdaQueryWrapper<StudentWork> studentWorkWrapper = new QueryWrapper<StudentWork>().lambda().eq(StudentWork::getStuId, studentId).eq(StudentWork::getCourseId, courseId);
        List<StudentWork> studentWorksList = Optional
                .ofNullable(studentWorkService.list(studentWorkWrapper))
                .orElseThrow(()->{return new NotSuchObjectException("studentWorksList对象为空");});
        ArrayList<StudentWork> hasNotFinishList = new ArrayList<>();
        ArrayList<StudentWork> alreadyTimeOutList = new ArrayList<>();
        ArrayList<StudentWork> waitSentenceList = new ArrayList<>();
        ArrayList<StudentWork> hasFinishList = new ArrayList<>();
        // 根据数据库的状态来排
        studentWorksList.forEach(work->{
            switch (WorkStatueEnum.getWorkStatueEnumByKey(work.getStatus())) {
                case HAS_NOT_FINISH :
                    hasNotFinishList.add(work);
                    break;
                case ALREADY_TIME_OUT :
                    alreadyTimeOutList.add(work);
                    break;
                case WAIT_SENTENCE :
                    waitSentenceList.add(work);
                    break;
                case HAS_FINISH :
                    hasFinishList.add(work);
                    break;};
    });
        List<StudentWork> sortedHasNotFinishWork = hasNotFinishList
                .stream()
                .sorted((work1, work2) -> work1.getEndTime().getTime() - work2.getEndTime().getTime() > 0 ? 1 : -1)
                .collect(Collectors.toList());
        List<StudentWork> sortedAlreadyTimeOutList = sortedOtherList(alreadyTimeOutList);
        List<StudentWork> sortedWaitSentenceList = sortedOtherList(waitSentenceList);
        List<StudentWork> sortedHasFinishList = sortedOtherList(hasFinishList);
        HashMap<String, List<StudentWork>> returnMap = new HashMap<>();
        returnMap.put("sortedHasNotFinishWork",sortedHasNotFinishWork);
        returnMap.put("sortedAlreadyTimeOutList",sortedAlreadyTimeOutList);
        returnMap.put("sortedWaitSentenceList",sortedWaitSentenceList);
        returnMap.put("sortedHasFinishList",sortedHasFinishList);
        return ResultBean.ok().setData(returnMap);
    }


    /**
     * @Description: 未完成作业详细内容的借口 0 1
     * @date: 2022/2/17 12:12
     * @Param: [workDetailDTO]
     * @return: org.linkworld.yuansystem.model.vo.ResultBean
    */
    @PostMapping("/workDetail")
    @ResponseBody
    @ApiOperation(value = "未完成作业详细内容的接口",notes = "返回未完成作业详细信息，data未workVO对象，包含workName,workDetail")
    public ResultBean getHomeWorkDetail(@RequestBody WorkDetailDTO dto) {
        WorkVo workVo = null;
            LambdaQueryWrapper<CourseWork> wrapper =
                    new QueryWrapper<CourseWork>().lambda()
                            .eq(CourseWork::getCourseId, dto.getCourseId()).eq(CourseWork::getWorkId,dto.getWorkId());
            CourseWork courseWork = Optional
                    .ofNullable(courseWorkService.getOne(wrapper))
                    .orElseThrow(() -> new NotSuchObjectException("courseWork对象为空"));
             workVo = new WorkVo(courseWork.getName(), courseWork.getWorkDescribe());
             return ResultBean.ok().setData(workVo);

    }

    @PostMapping("/seeWaitWork")  // 0
    @ResponseBody
    @ApiImplicitParams(@ApiImplicitParam(name = "studentWork",value = "将之前的studentWork对象传进来即可"))
    @ApiOperation(value = "点击待批阅的接口",notes = "Message中存有'等待教师批阅'的信息，data中存放work对象，用于传入查看作业")
    public ResultBean seeWaitWork(@RequestBody  StudentWork studentWork) {
        if(studentWork.getStatus()==WorkStatueEnum.HAS_FINISH.getStatue()) {
            return ResultBean.ok().setMessage("等待教师批阅").setData(studentWork);
        }
        return ResultBean.bad();
    }



    @PostMapping("/seeHasFinishSentence")  // 0
    @ResponseBody
    @ApiImplicitParams(@ApiImplicitParam(name = "studentWork",value = "将之前的studentWork对象传进来即可"))
    @ApiOperation(value = "点击已经完成作业的的接口",notes = "data中返回一个StudentWork对象，作业名称、分数、评语在里面拿")
    public ResultBean seeHasFinishSentence(@RequestBody StudentWork studentWork) {
        if(studentWork.getStatus()==WorkStatueEnum.HAS_FINISH_SENTENCE.getStatue()) {
            return ResultBean.ok().setData(studentWork);
        }
        return ResultBean.bad();
    }

    @PostMapping("/getCommitWork")  // 0
    @ResponseBody
    @ApiOperation(value = "查看学生提交作业的接口",
            notes = "data中存有一个List集合，list集合中的元素为File类型")
    public ResultBean getMyWork(@RequestBody StudentWork studentWork) {
        File workFile = new File(FileProperties.STUDENT_WORK_PATH);
        ArrayList<File> myCommitWork = new ArrayList<>();
        File[] workFileList = workFile.listFiles();
        for (File file : workFileList) {
            if(file.getName().equals(studentWork.getStuId().toString())) {
                System.out.println("找到学生目录了");
                File[] myTotalWorkFiles = file.listFiles();
                for (File myTotalWorkFile : myTotalWorkFiles) {
                    String workName = studentWork.getStuId()+"-"+studentWork.getCourseId()+"-"+studentWork.getWorkId();
                    if(myTotalWorkFile.getName().contains(workName)) {
                        myCommitWork.add(myTotalWorkFile);
                    }
                }
            }
        }
        return ResultBean.ok().setData(myCommitWork);
    }

    @PostMapping("/moreThanTimeWork")// 0
    @ResponseBody
    @ApiOperation(value = "已经过期作业的接口",
            notes = "在message存有'已过期'，data存截止日期")
    public ResultBean moreThanTimeWorkShow(@RequestBody StudentWork studentWork) {
        if(studentWork.getStatus()==WorkStatueEnum.ALREADY_TIME_OUT.getStatue()) {
            String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(studentWork.getEndTime());
            return ResultBean.ok().setMessage(WorkStatueEnum.ALREADY_TIME_OUT.getDescription()).setData(endTime);
        }
        return ResultBean.bad();
    }





    private List<StudentWork> sortedOtherList(List<StudentWork> courseWorkList) {
        List<StudentWork> sortedOtherList = courseWorkList
                .stream()
                .sorted((work1, work2) -> work1.getStartTime().getTime() - work2.getStartTime().getTime() > 0 ? 1 : 0)
                .collect(Collectors.toList());
        return sortedOtherList;

    }
}

