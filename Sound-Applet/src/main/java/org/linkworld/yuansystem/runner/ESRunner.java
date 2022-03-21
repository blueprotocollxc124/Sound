package org.linkworld.yuansystem.runner;

/*
 *@Author  LXC BlueProtocol
 *@Since   2022/2/14
 */


import com.alibaba.fastjson.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.linkworld.yuansystem.model.entity.Course;
import org.linkworld.yuansystem.properties.ESProperties;
import org.linkworld.yuansystem.service.CourseService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ESRunner implements CommandLineRunner {

 private final CourseService courseService;

 private final RestHighLevelClient restHighLevelClient;

 @Override
 public void run(String... args) throws Exception {
  boolean existsIndex = isExistsIndex(ESProperties.ES_INDEX_COURSE);
  if(existsIndex == false) {
   CreateIndexRequest createIndexRequest = new CreateIndexRequest(ESProperties.ES_INDEX_COURSE);
   CreateIndexResponse response = restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
   log.info("ES中创建索引{}成功",ESProperties.ES_INDEX_COURSE);
  }
  BulkRequest bulkRequest = new BulkRequest(ESProperties.ES_INDEX_COURSE);
  List<Course> courseList = courseService.list(null);
  for (int i = 0; i < courseList.size(); i++) {
   bulkRequest.add(
           new IndexRequest(ESProperties.ES_INDEX_COURSE)
                   .id(""+(i+1))
                   .source(JSON.toJSONString(courseList.get(i)),XContentType.JSON));
  }
  BulkResponse bulkResponse = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
  log.info("加载课程数据到ES索引{},操作{}！",ESProperties.ES_INDEX_COURSE,!bulkResponse.hasFailures());
 }

 /**
  * @Description: 判断ES中的index是否存在
  * @date: 2022/2/14 23:33
  * @Param: [indexName]
  * @return: boolean
 */
 public boolean isExistsIndex(String indexName) throws IOException {
  boolean exists = restHighLevelClient.indices().exists(new GetIndexRequest(ESProperties.ES_INDEX_COURSE), RequestOptions.DEFAULT);
  return exists;
 }
}
