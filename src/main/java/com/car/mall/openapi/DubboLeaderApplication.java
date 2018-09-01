package com.car.mall.openapi;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import sun.management.VMManagement;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.CountDownLatch;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class DubboLeaderApplication implements CommandLineRunner, DisposableBean {

    @Value(value = "${pid.path}")
    private String pidPath;

    private final static CountDownLatch latch = new CountDownLatch(1);
    private static ConfigurableApplicationContext context;

    private static Logger logger = LoggerFactory.getLogger(DubboLeaderApplication.class);

    public static void main(String[] args) throws InterruptedException {
        context = SpringApplication.run(DubboLeaderApplication.class, args);
        latch.await();
    }

    @Override
    public void destroy() throws Exception {
        latch.countDown();
        context.close();
        logger.info("服务提供者关闭------>>服务关闭");
    }

    @Override
    public void run(String... strings) throws Exception {
        File newFile = new File(pidPath);
        if(!FileUtil.exist(newFile)){
            FileUtil.touch(newFile);
        }
        FileWriter writer = new FileWriter(newFile);
        String pid = String.valueOf(jvmPid());
        writer.write(pid);
        logger.info("服务提供者启动完毕------>>启动完毕");
    }

    public static final int jvmPid() {
        try {
            RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
            Field jvm = runtime.getClass().getDeclaredField("jvm");
            jvm.setAccessible(true);
            VMManagement mgmt = (VMManagement) jvm.get(runtime);
            Method pidMethod = mgmt.getClass().getDeclaredMethod("getProcessId");
            pidMethod.setAccessible(true);
            int pid = (Integer) pidMethod.invoke(mgmt);
            return pid;
        } catch (Exception e) {
            return -1;
        }
    }

}