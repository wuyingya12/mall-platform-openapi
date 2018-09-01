package com.car.mall.openapi.service;/**
 * Created by Administrator on 2018/8/30.
 */

import com.car.api.IndexService;
import com.car.mall.openapi.base.ResponseEntity;
import com.car.mall.openapi.exceptions.InnerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description [首页服务]
 * @Author <a href="mailto: wuyingya@hipiao.com">吴迎亚</a>
 * @Date 2018/8/30 11:31
 **/
@Service
public class OpenIndexService {

    @Autowired
    IndexService indexService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public ResponseEntity<String> getDemo(String param) {
        try {
            return ResponseEntity.success(indexService.getDemo(param).getData().getInfo());
        } catch (Exception e) {
            logger.error("调用Dubbo服务异常：", e);
            throw new InnerException("调用Dubbo服务置异常", e);
        }
    }

}
