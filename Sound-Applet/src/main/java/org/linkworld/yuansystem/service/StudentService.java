package org.linkworld.yuansystem.service;

import org.linkworld.yuansystem.model.dto.StudentLoginDTO;
import org.linkworld.yuansystem.model.dto.StudentRegisterDTO;
import org.linkworld.yuansystem.model.entity.Student;
import com.baomidou.mybatisplus.extension.service.IService;
import org.linkworld.yuansystem.model.vo.ResultBean;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiuXiangCheng
 * @since 2022-01-18
 */
public interface StudentService extends IService<Student> {

     ResultBean studentLogin(StudentLoginDTO loginDTO,String openId);


     ResultBean studentRegister(StudentRegisterDTO registerDTO);

}
