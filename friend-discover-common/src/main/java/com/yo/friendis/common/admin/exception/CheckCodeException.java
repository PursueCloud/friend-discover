package com.yo.friendis.common.admin.exception;

import org.apache.shiro.authc.AuthenticationException;

/**
 * 验证码异常类
 * Created by Yo on 2017/5/8.
 */
public class CheckCodeException extends AuthenticationException {

    private static final long serialVersionUID = 1L;

    public CheckCodeException() {
        super();
    }

    public CheckCodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public CheckCodeException(String message) {
        super(message);
    }

    public CheckCodeException(Throwable cause) {
        super(cause);
    }

}
