package com.car.mall.openapi.controller.index;

import com.car.mall.openapi.base.ResponseEntity;
import com.car.mall.openapi.exceptions.InnerException;
import com.car.mall.openapi.service.OpenIndexService;
import com.google.common.base.Preconditions;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description [首页]
 * @Author <a href="mailto: wuyingya@hipiao.com">吴迎亚</a>
 * @Date 2018/8/30 11:30
 **/
@Api(tags = "商城首页")
@RestController
@RequestMapping("/mapi/mall/index")
public class IndexController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private OpenIndexService openIndexService;

    @ApiOperation("首页")
    @RequestMapping(value = "/demo", method = RequestMethod.POST)
//    @RequiresPermissions("index:demo")
    public ResponseEntity<String> getDemo(@RequestParam @ApiParam(value = "参数", required = true) String param) {
        Preconditions.checkNotNull(param, "参数不能为空");

        try {
            return openIndexService.getDemo(param);
        } catch (Exception e) {
            logger.error("获取首页信息异常：", e);
            throw new InnerException("获取首页信息异常", e);
        }

    }
}
