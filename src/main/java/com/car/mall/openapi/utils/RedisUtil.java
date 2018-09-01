package com.car.mall.openapi.utils;

import cn.vcfilm.sts.framework.redis.RedisSentinelWrapper;
import com.car.mall.openapi.base.shiro.SerializeUtils;
import com.car.mall.openapi.dao.RedisServerDaoImpl;
import com.car.mall.openapi.dao.entry.RedisServerInfo;
import org.apache.log4j.Logger;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RedisUtil {
    private static Logger logger = Logger.getLogger(RedisUtil.class);

    public static RedisSentinelWrapper redisWrapper = null;
    private static boolean initialized = false;

    private static Object syncInitialize = new Object();

    private static String SERVER_NAME = "juinke-platform-openapi";

    public static int PRO_CINEMA_LOGIN_SESSION_TIMEOUT;
    private static int CINEMA_LOGIN_SESSION_TIMEOUT;
    private static int CINEMA_LOGIN_TIMEOUT;

    @Value("${mall.account.session.pro.timeout}")
    public void setProTimeout(int proTimeout) {
        RedisUtil.PRO_CINEMA_LOGIN_SESSION_TIMEOUT = proTimeout;
    }

    @Value("${mall.account.session.timeout}")
    public void setTimeout(int timeout) {
        RedisUtil.CINEMA_LOGIN_SESSION_TIMEOUT = timeout;
    }

    @Value("${mall.account.login.timeout}")
    public void setLoginTimeout(int loginTimeout) {
        RedisUtil.CINEMA_LOGIN_TIMEOUT = loginTimeout;
    }



    //单位毫秒（7天）
    public final static int GLOBAL_SESSION_TIMEOUT = 1000*60*60*24*7;

    @Autowired
    private RedisServerDaoImpl redisServerDaoImpl;

    /**
     *  影院登录session
     **/
    private static String cinemaLoginSessionKey = "cinema:account:login:session:";
    /**
     *  影院登录
     **/
    private static String cinemaLoginKey = "cinema:account:login:";

    @PostConstruct
    public void initialize() {
        if (!initialized) {
            synchronized (syncInitialize) {
                if (!initialized) {
                    List<RedisServerInfo> list = redisServerDaoImpl.getRedisServerList();
                    redisWrapper = new RedisSentinelWrapper();
                    HashSet<String> shards = getShardInfos(list, SERVER_NAME);
                    if (shards.size() > 0) {
                        redisWrapper.initialize(shards);
                    }
                }
            }
        }
    }

    private HashSet<String> getShardInfos(
            List<RedisServerInfo> servers, String serviceName) {
        HashSet<String> shards = new HashSet<>();
        for (RedisServerInfo crs : servers) {
            if (crs.getService().equals(serviceName)) {
                String serviceAddress = String.format("%s:%s", crs.getHost(), crs.getPort());
                shards.add(serviceAddress);
            }
        }
        return shards;
    }

    public static RedisSentinelWrapper getRedisWrapper() {
        return redisWrapper;
    }

    public static void setRedisWrapper(RedisSentinelWrapper redisWrapper) {
        RedisUtil.redisWrapper = redisWrapper;
    }

    private static byte[] getByteKey(String keyPrefix,Serializable sessionId){
        String preKey = keyPrefix + sessionId;
        return preKey.getBytes();
    }

    /**
     *获取影院的session
     * @return
     */
    public static byte[] getCinemaLoginSessionKey(Serializable sessionId) {
        try {
            return redisWrapper.get(getByteKey(cinemaLoginSessionKey,sessionId));
        } catch (Exception e) {
            logger.error("获取影院的sessionKey失败", e);
        }
        return null;
    }

    //设置小程序用户登录的sessionKey
    public static void setCinemaLoginSessionKey(Session session) {
        try {
            Object sessionValue = SerializeUtils.deserialize(getCinemaLoginKey("*",session.getId().toString()));
            if(null != sessionValue){
                byte[] key = getByteKey(cinemaLoginSessionKey,session.getId());
                byte[] value = SerializeUtils.serialize(session);
                redisWrapper.set(key, value, PRO_CINEMA_LOGIN_SESSION_TIMEOUT);
            }else{
                byte[] key = getByteKey(cinemaLoginSessionKey,session.getId());
                byte[] value = SerializeUtils.serialize(session);
                redisWrapper.set(key, value,3000);
            }
        } catch (Exception e) {
            logger.error("设置影院登录的sessionKey失败", e);
        }
    }

    /**
     * 删除影院session
     * @param session
     */
    public static void deleteCinemaLoginSessionKey(Session session) {
        try {
            redisWrapper.del(getByteKey(cinemaLoginSessionKey,session.getId()));
        } catch (Exception e) {
            logger.error("删除影院session失败", e);
        }
    }

    /**
     * 获取有效的所有session
     * @return
     */
    public Set<Session> getActiveSessions(){
        Set<Session> sessions = new HashSet<Session>();

        Set<byte[]> keys = null;
        try {
            keys = redisWrapper.byteKeys(this.CINEMA_LOGIN_SESSION_TIMEOUT + "*");
            if(keys != null && keys.size()>0){
                for(byte[] key:keys){
                    Session s = (Session) SerializeUtils.deserialize(redisWrapper.get(key));
                    sessions.add(s);
                }
            }
        } catch (Exception e) {
            logger.error("获取有效的所有session失败", e);
        }
        return sessions;
    }


    /**
     *获取影院登录缓存
     * @return
     */
    public static byte[] getCinemaLoginKey(String accountName, String loginToken) {
        try {
            if(null == loginToken){
                Set<String> set = redisWrapper.keys(cinemaLoginKey+accountName+"-*");
                List<String> list = new ArrayList<String>(set);
                if(null != list && list.size()==1){
                    return redisWrapper.get(list.get(0).getBytes());
                }
                return null;
            }else if(null == accountName){
                Set<String> set = redisWrapper.keys(cinemaLoginKey+"*-"+loginToken);
                List<String> list = new ArrayList<String>(set);
                if(null != list && list.size()==1){
                    return redisWrapper.get(list.get(0).getBytes());
                }
                return null;
            }
            return redisWrapper.get(getByteKey(cinemaLoginKey,accountName+"-"+loginToken));
        } catch (Exception e) {
            logger.error("获取影院的sessionKey失败", e);
        }
        return null;
    }

    /**
     * 设置影院登录缓存，为了判断其过期时间
     * @param cinemaSession
     */
//    public void setCinemaLoginKey(CinemaSessionData cinemaSession) {
//        try {
//            Set<String> set = redisWrapper.keys(cinemaLoginKey+cinemaSession.getAccountName()+"-*");
//            if(set.size() > 0){
//                for (String key:set) {
//                    redisWrapper.delKeys(key);
//                }
//            }
//
//            byte[] key = getByteKey(cinemaLoginKey,cinemaSession.getAccountName()+"-"+cinemaSession.getLoginToken());
//            byte[] value = SerializeUtils.serialize(cinemaSession);
//            redisWrapper.set(key, value, CINEMA_LOGIN_TIMEOUT);
//        } catch (Exception e) {
//            logger.error("设置影院登录的sessionKey失败", e);
//        }
//    }



}
