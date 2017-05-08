package com.yo.friendis.common.shiro.filter;

import com.yo.friendis.common.shiro.token.UsernamePasswordCheckCodeToken;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 表单验证过滤器
 * Created by Yo on 2017/5/8.
 */
public class FormAuthenticationCheckCodeFilter extends FormAuthenticationFilter {

    public static final String DEFAULT_CHECK_CODE_PARAM = "checkCode";
    private String checkCode = DEFAULT_CHECK_CODE_PARAM;

    public String getCheckCodeParam() {
        return checkCode;

    }

    protected String getCheckCode(ServletRequest request) {
        return WebUtils.getCleanParam(request, getCheckCodeParam());
    }

    protected AuthenticationToken createToken(
            ServletRequest request, ServletResponse response) {

        String username = getUsername(request);
        String password = getPassword(request);
        String checkCode = getCheckCode(request);
        boolean rememberMe = isRememberMe(request);
        String host = getHost(request);

        return new UsernamePasswordCheckCodeToken(username,
                password.toCharArray(), rememberMe, host, checkCode);

    }
}

