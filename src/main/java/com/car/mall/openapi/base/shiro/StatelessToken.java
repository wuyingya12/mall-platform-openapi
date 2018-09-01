package com.car.mall.openapi.base.shiro;

import org.apache.shiro.authc.AuthenticationToken;

import java.util.Map;

public class StatelessToken implements AuthenticationToken {

	private String userName;
    private String clientDigest;
	// 预留参数集合，校验更复杂的权限
    private Map<String, ?> params;


    public StatelessToken(String userName, String clientDigest, Map<String, ?> params) {
        this.userName = userName;
        this.params = params;
        this.clientDigest = clientDigest;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public  Map<String, ?> getParams() {
        return params;
    }

    public void setParams( Map<String, ?> params) {
        this.params = params;
    }

    public String getClientDigest() {
        return clientDigest;
    }

    public void setClientDigest(String clientDigest) {
        this.clientDigest = clientDigest;
    }

    @Override
    public Object getPrincipal() {
       return userName;
    }

    @Override
    public Object getCredentials() {
        return clientDigest;
    }

}