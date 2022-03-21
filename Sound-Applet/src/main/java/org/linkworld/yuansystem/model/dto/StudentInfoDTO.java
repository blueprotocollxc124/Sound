package org.linkworld.yuansystem.model.dto;

/*
 *@Author  LXC BlueProtocol
 *@Since   2022/3/6
 */

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.linkworld.yuansystem.model.entity.Student;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentInfoDTO {

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "用户昵称")
    private String name;

    @ApiModelProperty(value = "个性签名")
    private String signature;

    @ApiModelProperty(value = "性别")
    private Integer sex;

    @ApiModelProperty(value = "头像地址")
    private String headAddr;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "组织码")
    private String groupNum;

    public StudentInfoDTO(Student student) {
        this.phone = student.getPhone();
        this.name = student.getName();
        this.signature = student.getSignature();
        this.sex = student.getSex();
        this.headAddr = student.getHeadAddr();
        this.email = student.getEmail();
        this.groupNum = student.getGroupNum();
    }


}
