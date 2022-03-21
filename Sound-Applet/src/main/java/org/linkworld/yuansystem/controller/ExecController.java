package org.linkworld.yuansystem.controller;


import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.linkworld.yuansystem.entity.Exec;
import org.linkworld.yuansystem.exception.NotSuchObjectException;
import org.linkworld.yuansystem.model.vo.ResultBean;
import org.linkworld.yuansystem.service.ExecService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * @author BlueProtocol
 * @since 2022-03-11
 */
@RestController
@RequestMapping("/applet/exec")
@Api(tags = "与练习相关的控制器", description = "包含获取所有的练习、练习详细等")
@RequiredArgsConstructor
public class ExecController {


    private final ExecService execService;

    @GetMapping("/getAllExec")
    @ResponseBody
    public ResultBean getAllExec() {
        List<Exec> execList = Optional.ofNullable(execService.list(null)).orElseThrow(() -> {
            return new NotSuchObjectException("没有这样的ExecList对象");
        });
        if(execList.size()==0) {
            return ResultBean.ok().setMessage("目前平台尚未发布任何练习");
        }
        return ResultBean.ok().setData(execList);
    }


    @PostMapping("/getExecDetail")
    @ResponseBody
    public ResultBean getExecDetail(@RequestBody Exec exec) {
        Exec execOption = Optional.ofNullable(exec).orElseThrow(() -> new NotSuchObjectException("没有这样的Exec对象"));
        return ResultBean.ok().setData(execOption);
    }






}

