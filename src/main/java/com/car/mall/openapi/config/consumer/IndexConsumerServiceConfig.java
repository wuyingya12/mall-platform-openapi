package com.car.mall.openapi.config.consumer;/**
 * Created by wu on 2018/7/17.
 */

import com.alibaba.dubbo.config.annotation.Reference;
import com.car.api.IndexService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName 对账服务
 * @Description TODO
 * @Author <a href="mailto: ouyangyang@hipiao.com">ouyangyang</a>
 * @Date 2017-07-18 16:04
 **/
@Configuration
public class IndexConsumerServiceConfig {

    @Reference
    private IndexService indexService;

    @Bean(name = "indexService")
    public IndexService getIndexService() {
        return indexService;
    }

}
