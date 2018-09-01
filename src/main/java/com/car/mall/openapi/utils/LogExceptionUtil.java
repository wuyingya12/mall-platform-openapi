package com.car.mall.openapi.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 用于打印详细日志，打印出具体的异长信息，方便日志查看
 */
public class LogExceptionUtil {
    public static String getTrace(Throwable t) {
        StringWriter stringWriter= new StringWriter();
        PrintWriter writer= new PrintWriter(stringWriter);
        t.printStackTrace(writer);
        StringBuffer buffer= stringWriter.getBuffer();
        return buffer.toString();
    }
}
