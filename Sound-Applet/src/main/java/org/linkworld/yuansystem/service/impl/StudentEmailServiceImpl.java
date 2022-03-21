package org.linkworld.yuansystem.service.impl;

import org.linkworld.yuansystem.entity.StudentEmail;
import org.linkworld.yuansystem.dao.StudentEmailMapper;
import org.linkworld.yuansystem.service.StudentEmailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author BlueProtocol
 * @since 2022-03-03
 */
@Service
public class StudentEmailServiceImpl extends ServiceImpl<StudentEmailMapper, StudentEmail> implements StudentEmailService {

    @Autowired
    private StudentEmailMapper studentEmailMapper;

    @Override
    public StudentEmail getSelectedEmail(BigInteger stuId,BigInteger emailId) {
        return studentEmailMapper.getSelectedEmail(stuId,emailId);
    }

    @Override
    public Boolean deleteByEmail(BigInteger stuId,BigInteger emailId) {
        return studentEmailMapper.deleteByEmail(stuId,emailId);
    }
}
