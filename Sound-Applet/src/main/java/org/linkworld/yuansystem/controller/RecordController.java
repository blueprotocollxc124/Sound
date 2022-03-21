package org.linkworld.yuansystem.controller;

/*
 *@Author  LXC BlueProtocol
 *@Since   2022/2/28
 */

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.linkworld.yuansystem.entity.Exec;
import org.linkworld.yuansystem.model.dto.GetRecordFromRedisDTO;
import org.linkworld.yuansystem.model.dto.RecordMP3ToRedisDTO;
import org.linkworld.yuansystem.model.entity.Student;
import org.linkworld.yuansystem.model.vo.ResultBean;
import org.linkworld.yuansystem.service.StudentService;
import org.linkworld.yuansystem.status.ReCordEnum;
import org.linkworld.yuansystem.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.validation.Valid;
import java.io.*;
import java.math.BigInteger;
import java.util.*;

@Controller
@Api(tags = {"与音频录制相关的控制器"})
@Slf4j
@RequestMapping("/applet/record")
public class RecordController extends BaseController{

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private Environment environment;

    @Autowired
    private StudentService studentService;

    @Autowired
    private IdUtil idUtil;


    @PostMapping("/record") // 0
    @ResponseBody
    @ApiOperation(value = "将本地mp3文件路径上传到Redis缓存的接口，用于学生录制音频之后使用",notes = "head加token")
    @ApiImplicitParams(@ApiImplicitParam(name = "recordMP3DTO", value = "recordMP3DTO对象", required = true))
    public ResultBean recordWork(@RequestBody @Valid RecordMP3ToRedisDTO recordMP3DTO) throws IOException {
        String token = request.getHeader("token");
        BigInteger studentIdBigInteger = idUtil.getStudentId(token);
        String studentId = studentIdBigInteger.toString();
        BigInteger workId = recordMP3DTO.getWorkId();
        BigInteger courseId = recordMP3DTO.getCourseId();
        Map<Object, Object> studentMap = redisUtil.hmget(studentId);
        Object courseIdMap = studentMap.get(courseId);
        if(courseIdMap==null) {
            Map<Object, Object> newCourseIdMap = new HashMap<>();
            Map<Object, Object> workContentMap = new HashMap<>();
            ReCordEnum enumByType = ReCordEnum.getEnumByType(recordMP3DTO.getType());
            String fileName = studentId+"-"+courseId+"-"+workId+"-"+enumByType.getDescription();
            FileUtil fileUtil = new FileUtil();
            String outFilePath = fileUtil.inAndOutFile(recordMP3DTO.getFile(), fileName);
            workContentMap.put(enumByType.getDescription(),outFilePath);
            newCourseIdMap.put(workId.toString(),workContentMap);
            studentMap.put(courseId.toString(),newCourseIdMap);
            redisUtil.hmset(studentId,MapUtil.objectToStringMap(studentMap));
            return ResultBean.ok();
        }
        else {
            studentMap = redisUtil.hmget(studentId);
            courseIdMap = studentMap.get(courseId);
            Map redisCourseIdMap = JsonUtil.string2Obj(courseIdMap.toString(), Map.class);
            Map redisWorkIdMap = JsonUtil.string2Obj(redisCourseIdMap.get(workId).toString(), Map.class);
            ReCordEnum enumByType = ReCordEnum.getEnumByType(recordMP3DTO.getType());
            String fileName = studentId+"-"+courseId+"-"+workId+"—"+enumByType.getDescription();
            FileUtil fileUtil = new FileUtil();
            String outFilePath = fileUtil.inAndOutFile(recordMP3DTO.getFile(), fileName);
            redisWorkIdMap.put(enumByType.getDescription(), outFilePath);
            redisCourseIdMap.put(workId.toString(), redisWorkIdMap);
            studentMap.put(courseId.toString(), redisCourseIdMap);
            redisUtil.hmset(studentId, MapUtil.objectToStringMap(studentMap));
            return ResultBean.ok();
        }
    }






    @PostMapping("/delRecord")// 0
    @ResponseBody
    @ApiOperation(value = "删除Redis缓存中存储的MP3文件路径的接口，用于学生删除音频之后使用",notes = "head加token")
    @ApiImplicitParams(@ApiImplicitParam(name = "recordMP3DTO", value = "recordMP3DTO对象", required = true))
    public ResultBean delete(@RequestBody @Valid GetRecordFromRedisDTO dto) {
        String token = request.getHeader("token");
        BigInteger studentIdBigInteger = idUtil.getStudentId(token);
        String studentId = studentIdBigInteger.toString();
        Map<Object, Object> studentMap = redisUtil.hmget(studentId);
        Object courseIdMap = redisUtil.hget(studentId,dto.getCourseId().toString());
        Map redisCourseIdMap = JsonUtil.string2Obj(courseIdMap.toString(), Map.class);
        Object workIdMap = redisCourseIdMap.get(dto.getWorkId().toString());
        Map redisWorkIdMap = (Map)workIdMap;
        redisWorkIdMap.remove(ReCordEnum.getEnumByType(dto.getType()).getDescription());
        redisCourseIdMap.replace(dto.getWorkId().toString(),redisWorkIdMap);
        studentMap.replace(dto.getCourseId().toString(),redisCourseIdMap);
        redisUtil.hmset(studentId,MapUtil.objectToStringMap(studentMap));
        return ResultBean.ok();
    }





}
