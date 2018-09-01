package com.car.mall.logbackExt.convert;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.car.mall.logbackExt.utils.IncrementIdContext;

/**
 * Created by zd.yao on 2017/5/24.
 */
public class IncrementIdConvert extends ClassicConverter {

    @Override
    public String convert(ILoggingEvent event) {
        return IncrementIdContext.get();
    }
}