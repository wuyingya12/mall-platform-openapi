package com.car.mall.openapi.base.shiro;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.car.mall.openapi.base.shiro.token.TokenProcessor;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;
import java.util.Map;

/**
 * @author gzp
 * @version 1.0
 * file is created in 2018/7/5
 */
public class StatelessSessionManager extends DefaultWebSessionManager {
    /**
     * 这个是服务端要返回给客户端，
     */
    public final static String TOKEN_NAME = "TOKEN";
    /**
     * 这个是客户端请求给服务端带的header
     */
    public final static String HEADER_TOKEN_NAME = "login-token";
    public final static Logger logger = LoggerFactory.getLogger(StatelessSessionManager.class);

    @Override
    protected Serializable getSessionId(ServletRequest servletRequest, ServletResponse servletResponse) {
        String token = WebUtils.toHttp(servletRequest).getHeader(HEADER_TOKEN_NAME);
        logger.info("获取到的请求token："+token);

        if(StringUtils.isEmpty(token)){
            String accountName = "";
            Map<String,String[]> params = servletRequest.getParameterMap();
            for (Map.Entry<String, String[]> entry : params.entrySet()) {
                if("account".equals(entry.getKey())){
                    accountName = entry.getValue()[0];
                    break;
                }
            }
            token = TokenProcessor.generateToken(accountName);
        }

        //这段代码还没有去查看其作用，但是这是其父类中所拥有的代码，重写完后我复制了过来...开始
        servletRequest.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE,
                ShiroHttpServletRequest.COOKIE_SESSION_ID_SOURCE);
        servletRequest.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, token);
        servletRequest.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
        return token;
    }

}
