package org.linkworld.yuansystem;
/*
 *@Author  LiuXiangCheng
 *@Since   2022/1/18  21:35
 */


import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.linkworld.yuansystem.model.entity.Student;
import org.linkworld.yuansystem.properties.ESProperties;
import org.linkworld.yuansystem.service.CourseService;
import org.linkworld.yuansystem.service.CourseWorkService;
import org.linkworld.yuansystem.service.StudentCourseService;
import org.linkworld.yuansystem.service.StudentService;
import org.linkworld.yuansystem.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import java.io.IOException;
import java.math.BigInteger;
import java.util.*;


@SpringBootTest
@RunWith(SpringRunner.class)
public class AnyTest {



   @Autowired
   private StudentService studentService;

   @Autowired
   private CourseService courseService;

   @Autowired
   private RedisUtil redisUtil;

   @Autowired
   RestHighLevelClient restHighLevelClient;

    @Autowired
    CourseWorkService courseWorkService;

    @Autowired
    StudentCourseService studentCourseService;

   @Test
    public void testMD5() throws IOException {
       CreateIndexRequest request = new CreateIndexRequest(ESProperties.ES_INDEX_COURSE);
       CreateIndexResponse response = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
   }

    @Test
    public void getStudent() {
        Student student = studentService.getById(1L);
        System.out.println(student);
    }
}
