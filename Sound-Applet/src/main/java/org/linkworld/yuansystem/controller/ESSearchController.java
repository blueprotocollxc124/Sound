package org.linkworld.yuansystem.controller;

/*
 *@Author  LXC BlueProtocol
 *@Since   2022/2/14
 */


import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.WrapperQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.linkworld.yuansystem.dao.StudentCourseMapper;
import org.linkworld.yuansystem.entity.CourseTeacher;
import org.linkworld.yuansystem.exception.NotSuchObjectException;
import org.linkworld.yuansystem.exception.NullParamException;
import org.linkworld.yuansystem.model.entity.Course;
import org.linkworld.yuansystem.model.entity.Student;
import org.linkworld.yuansystem.model.entity.Teacher;
import org.linkworld.yuansystem.model.vo.CourseVO;
import org.linkworld.yuansystem.model.vo.ResultBean;
import org.linkworld.yuansystem.properties.ESProperties;
import org.linkworld.yuansystem.service.*;
import org.linkworld.yuansystem.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@Api(tags = "???????????????????????????", description = "????????????????????????,??????????????????????????????????????????????????????")
@RequestMapping("/applet/search")
@RequiredArgsConstructor
public class ESSearchController  extends BaseController{


    private final RestHighLevelClient restHighLevelClient;

    private final RedisUtil redisUtil;

    private final CourseService courseService;

    private final CourseTeacherService courseTeacherService;

    private final TeacherService teacherService;

    private final StudentCourseService studentCourseService;

    private final StudentService studentService;

    private final IdUtil idUtil;






    /**
     * @Description: ES????????????????????? 0 1
     * @date: 2022/2/15 0:13
     *  @Param: [courseName]
     * @return: org.linkworld.yuansystem.model.vo.ResultBean
     * ????????????data?????????map(hasCourseList,notHasCourseList)
     */
    @ResponseBody
    @PostMapping("/course")
    @ApiOperation(value = "????????????????????????",notes = "head???token")
    @ApiImplicitParams(@ApiImplicitParam(name = "searchContent", value = "????????????", required = true))
    public ResultBean searchCourse(@Param("search") String search) throws IOException {
        Assert.hasText(search,"????????????????????????");
        String token = request.getHeader("token");
        BigInteger studentId = idUtil.getStudentId(token);
        String studentIdStr = studentId.toString();
        saveSearchContent(studentId,search);
        SearchRequest searchRequest = new SearchRequest(ESProperties.ES_INDEX_COURSE);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        MatchPhraseQueryBuilder matchPhraseQuery = QueryBuilders.matchPhraseQuery("courseName", search);
        sourceBuilder.query(matchPhraseQuery);
        searchRequest.source(sourceBuilder);
        SearchResponse esResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        ArrayList<Course> courseList = new ArrayList<>();
        esResponse.getHits().forEach(a -> {
            String courseString = a.getSourceAsString();
            Course course = JSON.parseObject(courseString, Course.class);
            courseList.add(course);
        });
        if (courseList.size() == 0) {
            return ResultBean.ok().setMessage("??????????????????");
        }
        // ????????? ????????? Mysql?????????  ??????????????????????????????  MySQL?????????????????????????????????
        ArrayList<CourseVO> hasCourseList = new ArrayList<>();
        ArrayList<CourseVO> notHasCourseList = new ArrayList<>();
        for (Course course : courseList) {
            String containStudents = course.getCourseStudent();
            if(containStudents.contains(studentIdStr)) {
                LambdaQueryWrapper<CourseTeacher> eq = new QueryWrapper<CourseTeacher>().lambda().eq(CourseTeacher::getCourseId, course.getCourseId());
                CourseTeacher courseTeacher = Optional
                                .ofNullable(courseTeacherService.getOne(eq))
                                .orElseThrow(() -> new NotSuchObjectException("??????CourseTeacher??????"));
                Teacher teacher = teacherService.getById(courseTeacher.getTeacherId());
                CourseVO courseVO = new CourseVO(course.getCourseId().toString(), course.getCourseName(), teacher.getTeachername(),course.getHeadAddr());
                hasCourseList.add(courseVO);
            }
            else {
                LambdaQueryWrapper<CourseTeacher> queryWrapper = new QueryWrapper<CourseTeacher>().lambda().eq(CourseTeacher::getCourseId, course.getCourseId());
                CourseTeacher courseTeacher = Optional
                        .ofNullable(courseTeacherService.getOne(queryWrapper))
                        .orElseThrow(() -> new NotSuchObjectException("??????CourseTeacher??????"));
                Teacher teacher = teacherService.getById(courseTeacher.getTeacherId());
                CourseVO courseVO = new CourseVO(course.getCourseId().toString(), course.getCourseName(), teacher.getTeachername(),course.getHeadAddr());
                notHasCourseList.add(courseVO);
            }
        }
        HashMap<String, List> map = new HashMap<>();
        map.put("hasCourseList",hasCourseList);
        map.put("notHasCourseList",notHasCourseList);
        return ResultBean.ok().setData(map);
    }

    /**
     * @Description: ??????????????????  0 1
     * @date: 2022/2/15 15:26
     * @Param: [studentId]
     * @return: ResultBean ?????????????????????data?????????String
     */
    @GetMapping("/recently")
    @ResponseBody
    @ApiOperation(value = "????????????????????????",notes = "head???token")
    public ResultBean searchRecently() {
        String token = request.getHeader("token");
        BigInteger studentId = idUtil.getStudentId(token);
        String studentIdStr = studentId.toString();
        String recentlySearchList = "recentlySearchList";
        ResultBean resultBean = searchRecentlyContent(studentIdStr, recentlySearchList);
        if(resultBean!=null) {
            return resultBean;
        }
        return ResultBean.ok();
    }

    /**
     * @Description: ??????????????????????????????  0 1
     * @date: 2022/2/15 15:26
     * @Param: [studentId]
     * @return: ResultBean ?????????????????????data?????????String
     */
    @GetMapping("/inMyCourseRecently")
    @ResponseBody
    @ApiOperation(value = "????????????????????????????????????",notes = "head???token")
    public ResultBean myCourseSearchRecently() {
        String token = request.getHeader("token");
        BigInteger studentId = idUtil.getStudentId(token);
        String studentIdStr = studentId.toString();
        String myCourseRecentlySearchList = "myCourseRecentlySearchList";
        ResultBean resultBean = searchRecentlyContent(studentIdStr, myCourseRecentlySearchList);
        if(resultBean!=null) {
            return resultBean;
        }
        return ResultBean.ok();
    }




    /**
     * @Description: ?????????????????? 0 1
     * @date: 2022/2/17 11:34
     * @Param: [studentId]
     * @return: org.linkworld.yuansystem.model.vo.ResultBean
    */
    @GetMapping("/recently/clear")
    @ResponseBody
    @ApiOperation(value = "??????????????????????????????",notes = "head???token")
    public ResultBean clearRecently() {
        String token = request.getHeader("token");
        BigInteger studentId = idUtil.getStudentId(token);
        String studentIdStr = studentId.toString();
        if (redisUtil.hasKey(studentIdStr)) {
            Map<Object, Object> studentIdMap = redisUtil.hmget(studentIdStr);
            List<String> recentlyList = (List<String>) studentIdMap.get("recentlySearchList");
            if(recentlyList==null) {
                return ResultBean.bad().setMessage("???????????????????????????");
            }
            recentlyList.clear();
            studentIdMap.replace("recentlySearchList",recentlyList);
            Map<String, Object> studentIdStrMap = MapUtil.objectToStringMap(studentIdMap);
            return ResultBean.ok().setSuccess(redisUtil.hmset(studentIdStr,studentIdStrMap));
        }
        return ResultBean.bad().setSuccess(false);
    }


    /**
     * @Description: ???????????????????????????????????? 0 1
     * @date: 2022/2/17 11:34
     * @Param: [studentId]
     * @return: org.linkworld.yuansystem.model.vo.ResultBean
     */
    @GetMapping("/recently/clearInMyCourse")
    @ResponseBody
    @ApiOperation(value = "????????????????????????????????????",notes = "head???token")
    public ResultBean clearInMyCourseRecently() {
        String token = request.getHeader("token");
        BigInteger studentId = idUtil.getStudentId(token);
        String studentIdStr = studentId.toString();
        String recentlyRegion = "myCourseRecentlySearchList";
        String newSearchCount = "newSearchCount";
        ResultBean resultBean = searchRecentlyClear(studentIdStr, recentlyRegion,newSearchCount);
        System.out.println(resultBean);
        if(resultBean!=null) {
            return resultBean;
        }
        return ResultBean.bad().setSuccess(false);
    }

    /**
     * @Description: ????????????????????????????????????????????????????????? 0
     * @date: 2022/2/17 17:28
     * @Param: [searchInMyCourseDTO]
     * @return: org.linkworld.yuansystem.model.vo.ResultBean
    */
    @PostMapping("/inMyCourseSearch")
    @ResponseBody
    @ApiOperation(value = "?????????????????????,?????????????????????????????????",notes = "head???token")
    public ResultBean searchInMyCourse(@Param("search") String search) {
        Assert.hasText(search,"????????????????????????");
       String token = request.getHeader("token");
        BigInteger studentId = idUtil.getStudentId(token);
        String myCourses = studentCourseService.getMyCourse(studentId);
        String[] split = myCourses.split(",");
        ArrayList<CourseVO> courseVoList = new ArrayList<>();
        for (int i = 0; i < split.length; i++) {
            Course course = courseService.getById(split[i]);
            LambdaQueryWrapper<CourseTeacher> wrappera = new QueryWrapper<CourseTeacher>().lambda()
                    .eq(CourseTeacher::getCourseId, course.getCourseId());
            CourseTeacher courseTeacher = courseTeacherService.getOne(wrappera);
            Teacher teacher = teacherService.getById(courseTeacher.getTeacherId());
            courseVoList.add(new CourseVO(course.getCourseId().toString(),course.getCourseName(),teacher.getTeachername(),course.getHeadAddr()));
        }
        List<CourseVO> filterCourseVOList = courseVoList.stream().filter(c -> {
            return c.getCourseName().contains(search);
        }).collect(Collectors.toList());
        saveInMyCourseSearchContent(studentId,search);
        return ResultBean.ok().setData(filterCourseVOList);
    }



    /**
     * @Description: ??????????????????????????????Redis????????? 0
     * @date: 2022/2/15 17:15
     * @Param: [bigInteger, searchContent]
     * @return: java.lang.Boolean
    */
    private void saveSearchContent(BigInteger bigInteger,String searchContent) {
        // ?????????????????????Redis???
        String studentIdStr = bigInteger.toString();
        if(redisUtil.hasKey(studentIdStr)) {
            Map<String, Object> studentIdMap = MapUtil.objectToStringMap(redisUtil.hmget(studentIdStr));
            List<String> recentlyList = (List<String>) studentIdMap.get("recentlySearchList");
            if(recentlyList==null) {
                 recentlyList = new ArrayList<String>();
                studentIdMap.put("recentlySearchList",recentlyList);
                redisUtil.hmset(studentIdStr,studentIdMap);
            }
            Integer searchCount = (Integer) studentIdMap.get("searchCount");
            if(recentlyList.size()==15) {
                if(searchCount==15) {
                    searchCount = Integer.valueOf(0);
                    recentlyList.set(searchCount,searchContent);
                    studentIdMap.replace("searchCount",searchCount);
                    redisUtil.hmset(studentIdStr,studentIdMap);

                }else {
                    recentlyList.set(searchCount,searchContent);
                    searchCount++;
                    studentIdMap.replace("searchCount",searchCount);
                    redisUtil.hmset(studentIdStr,studentIdMap);
                }


            }else {
                recentlyList.add(searchContent);
                studentIdMap.replace("recentlySearchList",recentlyList);
                redisUtil.hmset(studentIdStr,studentIdMap);
            }
        }
        else {
            HashMap<String, Object> studentIdMap = new HashMap<>();
            ArrayList<String> searchRecentlyList = new ArrayList<>();
            searchRecentlyList.add(searchContent);
            studentIdMap.put("recentlySearchList",searchRecentlyList);
            Integer searchCount = Integer.valueOf(0);
            studentIdMap.put("searchCount",searchCount);
            redisUtil.hmset(studentIdStr,studentIdMap);

        }

    }




    /**
     * @Description: ?????????????????????????????????????????????Redis????????? 0
     * @date: 2022/2/15 17:15
     * @Param: [bigInteger, searchContent]
     * @return: java.lang.Boolean
     */
    private void saveInMyCourseSearchContent(BigInteger bigInteger,String searchContent) {
        // ?????????????????????Redis???
        String studentIdStr = bigInteger.toString();
        if(redisUtil.hasKey(studentIdStr)) {
            Map<String, Object> studentIdMap = MapUtil.objectToStringMap(redisUtil.hmget(studentIdStr));
            List<String> recentlyList = (List<String>) studentIdMap.get("myCourseRecentlySearchList");
            if(recentlyList==null) {
                recentlyList = new ArrayList<String>();
                studentIdMap.put("myCourseRecentlySearchList",recentlyList);
                redisUtil.hmset(studentIdStr,studentIdMap);
            }
            Integer searchCount = (Integer) studentIdMap.get("newSearchCount");
            if(searchCount==null) {
                searchCount = Integer.valueOf(1);
                studentIdMap.put("newSearchCount",searchCount);
                redisUtil.hmset(studentIdStr,studentIdMap);
            }
            if(recentlyList.size()==15) {
                if(searchCount==15) {
                    searchCount = Integer.valueOf(0);
                    recentlyList.set(searchCount,searchContent);
                    searchCount++;
                    studentIdMap.replace("newSearchCount",searchCount);
                    redisUtil.hmset(studentIdStr,studentIdMap);
                }else {
                    recentlyList.set(searchCount,searchContent);
                    searchCount++;
                    studentIdMap.replace("newSearchCount",searchCount);
                    redisUtil.hmset(studentIdStr,studentIdMap);
                }


            }else {
                recentlyList.add(searchContent);
                studentIdMap.replace("newSearchCount",++searchCount);
                studentIdMap.replace("myCourseRecentlySearchList",recentlyList);
                redisUtil.hmset(studentIdStr,studentIdMap);
            }
        }
        else {
            HashMap<String, Object> studentIdMap = new HashMap<>();
            ArrayList<String> searchRecentlyList = new ArrayList<>();
            searchRecentlyList.add(searchContent);
            studentIdMap.put("myCourseRecentlySearchList",searchRecentlyList);
            Integer searchCount = Integer.valueOf(1);
            studentIdMap.put("newSearchCount",searchCount);
            redisUtil.hmset(studentIdStr,studentIdMap);

        }

    }


    public ResultBean searchRecentlyContent(String studentIdStr, String recentlyRegion) {
        if (redisUtil.hasKey(studentIdStr)) {
            Map<Object, Object> studentIdMap = redisUtil.hmget(studentIdStr);
            List<Object> recentlyList = (List<java.lang.Object>) studentIdMap.get(recentlyRegion);
            if(recentlyList==null) {
                return ResultBean.bad().setMessage("???????????????????????????");
            }
            return ResultBean.ok().setData(recentlyList);
        }
        return null;
    }


    public ResultBean searchRecentlyClear(String studentIdStr,String recentlyRegion,String count) {
        if (redisUtil.hasKey(studentIdStr)) {
            Map<Object, Object> studentIdMap = redisUtil.hmget(studentIdStr);
            List<String> recentlyList = (List<String>) studentIdMap.get(recentlyRegion);
            Integer newSearchCount = (Integer) studentIdMap.get(count);
            if(recentlyList==null&&newSearchCount==null) {
                return ResultBean.bad().setMessage("???????????????????????????");
            }
            recentlyList.clear();
            newSearchCount = 1;
            studentIdMap.replace("recentlySearchList",recentlyList);
            Map<String, Object> studentIdStrMap = MapUtil.objectToStringMap(studentIdMap);
            return ResultBean.ok().setSuccess(redisUtil.hmset(studentIdStr,studentIdStrMap));
        }
        return null;
    }




}
