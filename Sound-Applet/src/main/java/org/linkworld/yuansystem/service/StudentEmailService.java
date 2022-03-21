package org.linkworld.yuansystem.service;

import org.apache.ibatis.annotations.Select;
import org.linkworld.yuansystem.entity.StudentEmail;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigInteger;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author BlueProtocol
 * @since 2022-03-03
 */
public interface StudentEmailService extends IService<StudentEmail> {

    StudentEmail getSelectedEmail(BigInteger stuId,BigInteger emailId);

    Boolean deleteByEmail(BigInteger stuId,BigInteger emailId);

}
