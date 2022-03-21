package org.linkworld.yuansystem.dao;

import org.apache.ibatis.annotations.Mapper;
import org.linkworld.yuansystem.model.entity.Email;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author BlueProtocol
 * @since 2022-03-03
 */
@Mapper
public interface EmailMapper extends BaseMapper<Email> {

}
