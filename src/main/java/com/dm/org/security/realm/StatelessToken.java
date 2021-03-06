package com.dm.org.security.realm;


import java.util.Map;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SaltedAuthenticationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;


/**
 * @author 刘祥德 qq313700046@icloud.com .
 *@date created in 12:07 2017/7/17.
 * @description
 * @modified by:
 */

public class StatelessToken implements AuthenticationToken,SaltedAuthenticationInfo
{

    private String username;

    private Map<String, ?> params;

    private String clientDigest;

    public StatelessToken(String username, Map<String, ?> params, String clientDigest)
    {
        this.username = username;
        this.params = params;
        this.clientDigest = clientDigest;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public Map<String, ?> getParams()
    {
        return params;
    }

    public void setParams(Map<String, ?> params)
    {
        this.params = params;
    }

    @Override
    public Object getPrincipal()
    {
        return username;
    }

    @Override
    public PrincipalCollection getPrincipals() {
        return null;
    }

    public String getClientDigest() {
        return clientDigest;
    }


    @Override
    public Object getCredentials() {
        return getClientDigest();
    }


    @Override
    public ByteSource getCredentialsSalt() {
        return null;
    }
}