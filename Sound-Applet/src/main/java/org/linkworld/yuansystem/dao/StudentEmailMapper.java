package org.linkworld.yuansystem.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.linkworld.yuansystem.entity.StudentEmail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.math.BigInteger;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author BlueProtocol
 * @since 2022-03-03
 */
@Mapper
public interface StudentEmailMapper extends BaseMapper<StudentEmail> {

    @Select("select * from student_email where stu_id = #{stuId} and email_id = #{emailId} ")
    StudentEmail getSelectedEmail(BigInteger stuId,BigInteger emailId);

    @Delete("delete from student_email where stu_id = #{stuId} and  email_id = #{emailId}")
    Boolean deleteByEmail(BigInteger stuId,BigInteger emailId);
}
