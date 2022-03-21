package org.linkworld.yuansystem.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


/*
 *@Author  LXC BlueProtocol
 *@Since   2022/2/15
 */

@Configuration
public class RedisConfig {

@Bean
public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {

  // 我们为了我们的开发方便，一般直接使用我们的<String, Object>
  RedisTemplate<String, Object> template = new RedisTemplate();
  // 连接工厂
  template.setConnectionFactory(redisConnectionFactory);
  // 序列化配置
  Jackson2JsonRedisSerializer<Object> Jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
  // jackson序列化的时候，我们需要使用名为的ObjectMapper来实现我们的转义，转完之后我们就可以使用了
  ObjectMapper objectMapper = new ObjectMapper();
  objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
  objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
  Jackson2JsonRedisSerializer.setObjectMapper(objectMapper);


  // 第二个我们还需要序列化我们的String类型
  // 所有的key都采用我们String序列化的方式
  StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
  template.setKeySerializer(stringRedisSerializer);
  // 所有的hash的key也采用String的序列化方式
  template.setHashKeySerializer(stringRedisSerializer);


  // 所有的value都采用我们自定义的Jackson的方式序列化
  template.setValueSerializer(Jackson2JsonRedisSerializer);
  // 所有hash的value也采用我们自定义Jackson的方式序列化、
  template.setHashValueSerializer(Jackson2JsonRedisSerializer);
  // 然后把所有的Properties设置进去
  template.afterPropertiesSet();
  return template;
}

}