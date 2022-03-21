package org.linkworld.yuansystem.dao;

import org.apache.ibatis.annotations.Mapper;
import org.linkworld.yuansystem.model.entity.Student;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LiuXiangCheng
 * @since 2022-01-18
 */
@Mapper
public interface StudentMapper extends BaseMapper<Student> {

}
