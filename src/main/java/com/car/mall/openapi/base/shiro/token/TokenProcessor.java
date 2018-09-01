package com.car.mall.openapi.base.shiro.token;

import cn.vcfilm.sts.framework.util.Md5Util;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 默认Token处理器提供将cooke和TokenParameter相互转换,Token生成的能力
 * <p>
 * 可以注册多个实例
 * </p>
 */
@Service
public class TokenProcessor {
    private static Logger log = LoggerFactory.getLogger(TokenProcessor.class);

    // 默认迭代次数
    private int hashIterations = 2;

    private static String localSeedValue = "123456";

    public int getHashIterations() {
        return hashIterations;
    }

    public void setHashIterations(int hashIterations) {
        this.hashIterations = hashIterations;
    }

    public static String generateToken(String accountName) {
        try {
            String seed = findSeed();
            String token = Md5Util.md5Sign(accountName + String.valueOf(System.currentTimeMillis()) + seed);
            return Base64.encodeBase64URLSafeString(org.apache.commons.codec.binary.StringUtils.getBytesUtf8(token));
        } catch (Exception e) {
            log.error("生成token异常", e);
            throw new IllegalArgumentException("生成token失败!");
        }
    }

    /**
     * 获得当前系统的 token seed
     */
    public static String findSeed(){
        if(localSeedValue != null){
            return localSeedValue;
        } else {
//            String seed = TokenGenerator.genSeed();
            String seed = "1234567890";
            localSeedValue = seed;
            return seed;
        }
    }

}
