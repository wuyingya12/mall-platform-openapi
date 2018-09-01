package com.car.mall.openapi.utils;
import cn.vcfilm.sts.framework.commons.StringUtil;
import cn.vcfilm.sts.framework.http.HttpServiceUtil;
import com.alibaba.fastjson.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 微信分享接口
 * Created by xiebj on 2016/7/22.
 */
public class WechatShare {

    //获取普通access_token URL
    private static String tokenUrl = "https://api.weixin.qq.com/cgi-bin/token";
    //获取jsapi_ticket URL
    private static String tiketUrl = "https://api.weixin.qq.com/cgi-bin/ticket/getticket";

    /**
     * 获取普通access_token
     * @param appId
     * @param secret
     * @return
     */
    public static String getToken(String appId,String secret){
        //全局缓存access_token 有效期7200秒（2小时）
        String accessToken = "";
        if(StringUtil.isNotEmpty(CacheMap.get("access_token"))){
            String accessTokenStr[] = CacheMap.get("access_token").split("\\|");
            if(System.currentTimeMillis() - Long.parseLong(accessTokenStr[1]) < 7200000L){
                accessToken = accessTokenStr[0];
            }
        }
        if(StringUtil.isEmpty(accessToken)){
            Map<String,Object> params = new HashMap<String,Object>();
            params.put("grant_type","client_credential");
            params.put("appid",appId);
            params.put("secret",secret);
            JSONObject result = HttpServiceUtil.service(tokenUrl, params);
            accessToken = result.getString("access_token");
            Long time = System.currentTimeMillis();
            String accessTokenT = accessToken+"|"+time;
            CacheMap.put("access_token", accessTokenT);
        }
        return accessToken;
    }

    /**
     * 获取jsapi_ticket
     * @param accessToken
     * @return
     */
    public static String getTiket(String accessToken){
        //全局缓存jsapi_ticket 有效期7200秒（2小时）
        String tiket = "";
        if (StringUtil.isNotEmpty(CacheMap.get("jsapi_ticket"))){
            String tiketStr[] = CacheMap.get("jsapi_ticket").split("\\|");
            if(System.currentTimeMillis() - Long.parseLong(tiketStr[1]) < 7200000L){
                tiket = tiketStr[0];
            }
        }
        if (StringUtil.isEmpty(tiket)) {
            Map<String,Object> params = new HashMap<String,Object>();
            params.put("access_token",accessToken);
            params.put("type","jsapi");
            JSONObject result = HttpServiceUtil.service(tiketUrl, params);
            tiket = result.getString("ticket");
            Long time = System.currentTimeMillis();
            String tiketT = tiket+"|"+time;
            CacheMap.put("jsapi_ticket", tiketT);
        }
        return tiket;
    }

    /**
     * 获取验签字符串
     * @param url 当前网页的URL，不包含#及其后面部分
     * @return
     */
    public static JSONObject getSign(String url, String appId, String secret){
        Long timestamp = System.currentTimeMillis()/1000;
        String noncestr = getRandomString(16);
        String accessToken = getToken(appId,secret);
        String jsapiTicket = getTiket(accessToken);
        String signStr = "jsapi_ticket="+jsapiTicket+"&noncestr="+noncestr+"&timestamp="+timestamp+"&url="+url;
        String signature = SHA1(signStr);
        JSONObject json = new JSONObject();
        json.put("appid",appId);
        json.put("nonceStr",noncestr);
        json.put("signature",signature);
        json.put("timestamp",timestamp);
        return json;
    }

    /**
     * 获取随机字符串
     * @param length
     * @return
     */
    public static String getRandomString(int length) {
        StringBuffer buffer = new StringBuffer("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        int range = buffer.length();
        for (int i = 0; i < length; i ++) {
            sb.append(buffer.charAt(random.nextInt(range)));
        }
        return sb.toString();
    }

    /**
     * SHA1算法
     * @param decript
     * @return
     */
    public static String SHA1(String decript) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(decript.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
