package com.car.mall.openapi.dao;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.car.mall.openapi.dao.entry.RedisServerInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * redis配置读取
 */
@Service
public class RedisServerDaoImpl {

    private Logger logger = Logger.getLogger(RedisServerDaoImpl.class);
    @Autowired
    public RedisServerDao redisServerDao;

    public List<RedisServerInfo> getRedisServerList() {
        try {
            List<RedisServerInfo> list = redisServerDao.getRedisServerList() == null ? new ArrayList<>() : redisServerDao.getRedisServerList();
            return list;
        } catch (Exception exp) {
            logger.error("读取redis配置异常", exp);
        }
        return null;

    }
}
