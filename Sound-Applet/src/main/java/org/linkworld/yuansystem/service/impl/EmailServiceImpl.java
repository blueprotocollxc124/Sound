package org.linkworld.yuansystem.service.impl;

import org.linkworld.yuansystem.model.entity.Email;
import org.linkworld.yuansystem.dao.EmailMapper;
import org.linkworld.yuansystem.service.EmailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author BlueProtocol
 * @since 2022-03-03
 */
@Service
public class EmailServiceImpl extends ServiceImpl<EmailMapper, Email> implements EmailService {

}
