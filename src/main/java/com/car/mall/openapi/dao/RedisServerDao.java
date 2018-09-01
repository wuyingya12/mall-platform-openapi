package com.car.mall.openapi.dao;

import com.car.mall.openapi.dao.entry.RedisServerInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RedisServerDao {

    /**
     * 获取redis服务配置列表
     * @return
     */
    List<RedisServerInfo> getRedisServerList();

}
