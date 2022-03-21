package org.linkworld.yuansystem.model.vo;

/*
 *@Author  LXC BlueProtocol
 *@Since   2022/3/3
 */

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.linkworld.yuansystem.model.entity.Email;

import java.math.BigInteger;
import java.util.Date;
import java.util.Locale;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class EmailVO {

 private BigInteger emailId;

 private BigInteger teacherId;

 private String title;

 private String content;

 @JsonFormat(locale="zh",timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
 private Date gmtCreate;

 private Integer status;

 private String picturePath;

 public EmailVO(Email email, Integer status) {
  this.emailId = email.getEmailId();
  this.title = email.getTitle();
  this.content = email.getContent();
  this.teacherId =email.getTeacherId();
  this.picturePath = email.getPicturePath();
  this.gmtCreate= email.getGmtCreate();
  this.status = status;
 }




}
