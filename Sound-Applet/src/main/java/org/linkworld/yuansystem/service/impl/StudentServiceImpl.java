package org.linkworld.yuansystem.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.api.R;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.linkworld.yuansystem.model.dto.StudentLoginDTO;
import org.linkworld.yuansystem.model.dto.StudentRegisterDTO;
import org.linkworld.yuansystem.model.entity.Student;
import org.linkworld.yuansystem.dao.StudentMapper;
import org.linkworld.yuansystem.model.vo.ResultBean;
import org.linkworld.yuansystem.properties.WeiXinProperties;
import org.linkworld.yuansystem.service.StudentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.linkworld.yuansystem.util.Md5Util;
import org.linkworld.yuansystem.util.ThrowableUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LiuXiangCheng
 * @since 2022-01-18
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {



    /**
     * MD5加密密码
     * @param entity
     * @return
     */
    @Override
    public boolean save(Student entity) {
        String password = entity.getPassword();
        String md5Password = Md5Util.md5(password);
        entity.setPassword(md5Password);
        return super.save(entity);
    }

    @Override
    public ResultBean studentLogin(StudentLoginDTO loginDTO,String openId) {
        ByteSource salt = ByteSource.Util.bytes(loginDTO.getPhone());
        Md5Hash md5Password = new Md5Hash(loginDTO.getPassword(),salt,1);
        UsernamePasswordToken token = new UsernamePasswordToken(loginDTO.getPhone(), md5Password.toString());
        token.setHost(openId);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
            return ResultBean.ok();
        } catch (UnknownAccountException e) {
            System.out.println(ThrowableUtil.getStackTrance(e));
            return ResultBean.bad().setMessage("用户不存在");
        } catch (IncorrectCredentialsException e) {
            System.out.println(ThrowableUtil.getStackTrance(e));
            return ResultBean.bad().setMessage("密码错误");
        } catch (LockedAccountException e) {
            System.out.println(ThrowableUtil.getStackTrance(e));
            return ResultBean.bad().setMessage("登录次数过多");
        }
    }

    @Override
    public ResultBean studentRegister(StudentRegisterDTO registerDTO) {
        ByteSource salt = ByteSource.Util.bytes(registerDTO.getPhone());
        Md5Hash passwordMD5 = new Md5Hash(registerDTO.getPassword(),salt,1);
        Student registerStudent = new Student()
                .setName(registerDTO.getName())
                .setPassword(passwordMD5.toString())
                .setPhone(registerDTO.getPhone())
                .setEmail(registerDTO.getEmail())
                .setGroupNum(registerDTO.getGroupNum());
        save(registerStudent);
        return ResultBean.ok();
    }
}
