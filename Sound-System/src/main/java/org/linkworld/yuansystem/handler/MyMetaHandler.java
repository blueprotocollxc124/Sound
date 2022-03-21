package org.linkworld.yuansystem.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

//不要忘记将这个处理类放到我们的IOC容器中
@Slf4j
@Component
public class MyMetaHandler implements MetaObjectHandler {
//插入时候的策略
@Override
public void insertFill(MetaObject metaObject) {
  this.setFieldValByName("gmtCreate",new Date(),metaObject);
  this.setFieldValByName("gmtModified",new Date(),metaObject);
}
//更新时候的策略
@Override
public void updateFill(MetaObject metaObject) {
  this.setFieldValByName("gmtModified",new Date(),metaObject);
}
}