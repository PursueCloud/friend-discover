package com.yo.friendis.common.shiro.token;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * 用户认证token
 * Created by Yo on 2017/5/8.
 */
public class UsernamePasswordCheckCodeToken extends UsernamePasswordToken {

    private static final long serialVersionUID = 1L;

    private String checkCode;

    public String getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }

    public UsernamePasswordCheckCodeToken() {
        super();
    }

    public UsernamePasswordCheckCodeToken(String username, char[] password,
                                        boolean rememberMe, String host, String checkCode) {
        super(username, password, rememberMe, host);
        this.checkCode = checkCode;
    }

}
