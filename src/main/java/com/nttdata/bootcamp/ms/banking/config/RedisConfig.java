/*package com.nttdata.bootcamp.ms.banking.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
@EnableCaching
public class RedisConfig {


  @Bean(destroyMethod = "shutdown")
  public RedissonClient redissonClient() {
    var config = new Config();
    config.useSingleServer()
        .setAddress("")
        .setPassword("");
    return Redisson.create(config);
  }

  @Bean
  @Autowired
  public CacheManager cacheManager(RedissonClient redissonClient) {
    var config = Collections.singletonMap("customers", new CacheConfig());
    return new RedissonSpringCacheManager(redissonClient, config);
  }

}*/