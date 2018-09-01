package com.car.mall.openapi.base.shiro;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.springframework.stereotype.Component;
import com.car.mall.openapi.base.shiro.token.TokenProcessor;

import java.io.Serializable;

/**
 * @author gzp
 * @version 1.0
 * file is created in 2018/7/6
 */
@Component
public class UuidSessionIdGenerator implements SessionIdGenerator {

    @Override
    public Serializable generateId(Session session) {
        Serializable uuid = new JavaUuidSessionIdGenerator().generateId(session);
        Serializable token = TokenProcessor.generateToken(uuid.toString());
        return token;
    }
}
