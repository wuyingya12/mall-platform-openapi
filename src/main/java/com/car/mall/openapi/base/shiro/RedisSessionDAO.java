package com.car.mall.openapi.base.shiro;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.car.mall.openapi.utils.RedisUtil;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

@Service
public class RedisSessionDAO extends AbstractSessionDAO {

    private static Logger logger = LoggerFactory.getLogger(RedisSessionDAO.class);
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private UuidSessionIdGenerator sessionIdGenerator;

    @Override
    protected Serializable generateSessionId(Session session) {
        return sessionIdGenerator.generateId(session);
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
        this.saveSession(session);
        logger.debug("-----------update session---------");
    }

    /**
     * save session
     * @param session
     * @throws UnknownSessionException
     */
    private void saveSession(Session session) throws UnknownSessionException{

        if(session == null || session.getId() == null){
            logger.error("session or session id is null");
            return;
        }

        redisUtil.setCinemaLoginSessionKey(session);
    }

    @Override
    public void delete(Session session) {
        if(session == null || session.getId() == null){
            logger.error("session or session id is null");
            return;
        }
        redisUtil.deleteCinemaLoginSessionKey(session);
        logger.debug("-----------delete session---------");
    }

    @Override
    public Collection<Session> getActiveSessions() {
        Set<Session> sessions = redisUtil.getActiveSessions();

        return sessions;
    }

    @Override
    protected Serializable doCreate(Session session) {

        Serializable sessionId = this.generateSessionId(session);
        this.assignSessionId(session, sessionId);
        this.saveSession(session);

        logger.debug("-----------doCreate session---------");
        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        if(sessionId == null){
            logger.error("session id is null");
            return null;
        }

        Session s = (Session) SerializeUtils.deserialize(redisUtil.getCinemaLoginSessionKey(sessionId));
        logger.debug("-----------doReadSession session---------");
        return s;
    }

}
