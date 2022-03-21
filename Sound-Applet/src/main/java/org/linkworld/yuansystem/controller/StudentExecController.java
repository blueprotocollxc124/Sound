package org.linkworld.yuansystem.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.util.StringUtils;
import org.linkworld.yuansystem.entity.Exec;
import org.linkworld.yuansystem.entity.StudentExec;
import org.linkworld.yuansystem.model.dto.StudentRecordExecDTO;
import org.linkworld.yuansystem.model.entity.Student;
import org.linkworld.yuansystem.model.vo.ResultBean;
import org.linkworld.yuansystem.service.ExecService;
import org.linkworld.yuansystem.service.StudentExecService;
import org.linkworld.yuansystem.service.StudentService;
import org.linkworld.yuansystem.status.ReCordEnum;
import org.linkworld.yuansystem.status.RecordExecEnum;
import org.linkworld.yuansystem.util.*;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author BlueProtocol
 * @since 2022-03-11
 */
@RestController
@RequestMapping("/applet/studentExec")
@Slf4j
@Api(tags = "与学生练习相关的控制器", description = "包含上传学生练习、删除练习，批量删除，获取学生上传的所有练习等")
@RequiredArgsConstructor
public class StudentExecController extends BaseController{

    private final StudentService studentService;

    private final StudentExecService studentExecService;

    private final Environment environment;

    private final ExecService execService;

    private final IdUtil idUtil;

    @PostMapping("/loadExeRecode") // 0
    @ResponseBody
    @ApiOperation(value = "上传练习的接口",notes = "head加token")
    @ApiImplicitParams(@ApiImplicitParam(name = "StudentRecordExecDTO",value = "StudentRecordExecDTO对象"))
    public ResultBean recodeExec( @RequestParam(value="file") MultipartFile file) {
        String fileSize = MultipartFileUtil.computeFileSize(file);
        String execId = request.getParameter("execId");
        String code = request.getParameter("code");
        if(!StringUtils.hasText(execId)||!StringUtils.hasText(code)) {
            return ResultBean.bad().setMessage("execId或者是code不能为空");
        }
        Integer codeInteger = Integer.valueOf(code);
        BigInteger execIdInteger = new BigInteger(execId);
        String token = request.getHeader("token");
        BigInteger studentIdBigInteger = idUtil.getStudentId(token);
        String studentId = studentIdBigInteger.toString();
        log.info(AnsiOutput.toString(AnsiColor.BRIGHT_BLUE,"学生ID："+studentId+",上传的练习文件的大小为"+fileSize+"M"));
        //String fileName = studentId + "-"+dto.getExecId()+"-"+dto.getCode()+"-"+RecordExecEnum.getExecEnumByType(dto.getCode()).getDescribe()+"exec";
        String fileName = studentId + "-"+execId+"-"+code+"—"+RecordExecEnum.getExecEnumByType(codeInteger).getDescribe()+"exec";
        String outFileName = FileUtil.inAndOutFile(file, fileName);
        StudentExec studentExec = new StudentExec()
                .setStudentId(studentIdBigInteger)
                .setExecId(execIdInteger)
                .setCode(codeInteger)
                .setSourceFileName(file.getOriginalFilename())
                .setFilePath(outFileName);
        studentExecService.save(studentExec);
        return  ResultBean.ok();
    }


    @Transactional
    @PostMapping("/deleteExec")  // 0
    @ResponseBody
    @ApiOperation(value = "删除练习的接口",notes = "head加token")
    @ApiImplicitParams(@ApiImplicitParam(name = "execId",value = "练习对象的id"))
    public ResultBean deleteRecordExec(@RequestBody @Valid StudentRecordExecDTO studentRecordExecDTO) {
        String token = request.getHeader("token");
        BigInteger studentIdBigInteger = idUtil.getStudentId(token);
        LambdaQueryWrapper<StudentExec> removeWrapper = new QueryWrapper<StudentExec>().lambda()
                .eq(StudentExec::getStudentId, studentIdBigInteger)
                .eq(StudentExec::getExecId, studentRecordExecDTO.getExecId())
                .eq(StudentExec::getCode,studentRecordExecDTO.getCode());
        StudentExec studentExecObject = studentExecService.getOne(removeWrapper);
        File file = new File(studentExecObject.getFilePath());
        boolean delete = file.delete();
        boolean remove = studentExecService.remove(removeWrapper);
        if(delete==true&&remove==true) {
            return ResultBean.ok();
        }else {
            return ResultBean.bad();
        }
    }


    @Transactional
    @PostMapping("/deleteSelectedExec")  // 0
    @ResponseBody
    @ApiOperation(value = "删除选中练习的接口",notes = "head加token")
    @ApiImplicitParams(@ApiImplicitParam(name = "studentExecList",value = "练习对象的集合"))
    public ResultBean deleteSelectedRecordExec(@RequestBody List<StudentExec> studentExecList) {
        Assert.notNull(studentExecList,"studentExecList不能为空");
        String token = request.getHeader("token");
        BigInteger studentIdBigInteger = idUtil.getStudentId(token);
        if(studentExecList.size()==0) {
            return ResultBean.ok();
        }
        //String studentId = studentIdBigInteger.toString();
        for (StudentExec studentExec : studentExecList) {
            LambdaQueryWrapper<StudentExec> deleteWrapper = new QueryWrapper<StudentExec>().lambda().eq(StudentExec::getStudentId, studentExec.getStudentId())
                    .eq(StudentExec::getExecId, studentExec.getExecId())
                    .eq(StudentExec::getCode, studentExec.getCode());
            System.out.println(studentExec.getFilePath());
            boolean remove = studentExecService.remove(deleteWrapper);
            new File(studentExec.getFilePath()).delete();
        }
        return ResultBean.ok();
    }


    @GetMapping("/getStudentAllExecRecord")  // 0
    @ResponseBody
    @ApiOperation(value = "获取学生上传练习的接口",notes = "head加token，data里面存一个List集合，集合包含一个个File对象")
    public ResultBean getStudentExecRecord() {
        String token = request.getHeader("token");
        BigInteger studentIdBigInteger = idUtil.getStudentId(token);
        String studentId = studentIdBigInteger.toString();
        Profiles dev = Profiles.of("dev");
        File myExecFile = null;
        if(environment.acceptsProfiles(dev)) {
            try {
                myExecFile =  new File("D:\\IDEA__project\\Backend\\Sound-System\\src\\main\\resources\\static\\work\\"+studentId+"\\exec");
            } catch (Exception e) {
                log.error("学生ID:{}——练习文件夹没有找到",studentId);
                return ResultBean.ok().setMessage("您还没有做任何练习哦");
            }
        }else {
            try {
                myExecFile =  new File(File.separator+"opt"+File.separator+"jar"+File.separator+"work"+File.separator+studentId+File.separator+"exec");
            } catch (Exception e) {
                log.error("学生ID:{}——练习文件夹没有找到",studentId);
                return ResultBean.ok().setMessage("您还没有做任何练习哦");
            }
        }
        List<File> execFileList = Arrays.asList(myExecFile.listFiles());
        return ResultBean.ok().setData(execFileList);

    }

    @PostMapping("/execSearch")
    @ResponseBody
    @ApiOperation(value = "搜索练习的接口")
    @ApiImplicitParams(@ApiImplicitParam(name = "search", value = "搜索的内容"))
    public ResultBean searchExec(@Param("search")String search) {
        if(!StringUtils.hasText(search)) {
           return ResultBean.bad().setMessage("搜索的内容不能为空");
        }
        List<Exec> list = execService.list(null);
        if(list==null) {
            return ResultBean.bad().setMessage("练习列表为null");
        }
        List<Exec> searchList = list.stream().filter(exec -> {
            return exec.getName().contains(search);
        }).collect(Collectors.toList());
        return ResultBean.ok().setData(searchList);
    }













}

