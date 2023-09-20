package com.project.tryticket.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
//@EnablxeRedisRepositories
public class RedisConfig {
   @Value("${spring.redis.host}")
   private String host;

   @Value("${spring.redis.port}")
   private int port;

   /**
    * 내장 혹은 외부의 Redis 연결
    */
   @Bean
   public RedisConnectionFactory redisConnectionFactory() {
      return new LettuceConnectionFactory(host, port);
   }

   /**
    * RedisConnection에서 넘겨준값 객체 직렬화
    * java Object를 redis에 저장하는 경우 사용
    */
   @Bean
   public RedisTemplate<?, ?> redisTemplate() {
      RedisTemplate<byte[], byte[]> template = new RedisTemplate<>();
      template.setConnectionFactory(redisConnectionFactory());

      template.setKeySerializer(new StringRedisSerializer());
      template.setValueSerializer(new Jackson2JsonRedisSerializer<>(String.class));

      return template;
   }

   /**
    * 일반적인 String 값을 key, value로 사용하는 경우
    */
   @Bean
   public StringRedisTemplate stringRedisTemplate(){
      StringRedisTemplate stringRedisTemplate=new StringRedisTemplate();
      stringRedisTemplate.setKeySerializer(new StringRedisSerializer());
      stringRedisTemplate.setValueSerializer(new StringRedisSerializer());
      stringRedisTemplate.setConnectionFactory(redisConnectionFactory());
      return stringRedisTemplate;
   }
}
