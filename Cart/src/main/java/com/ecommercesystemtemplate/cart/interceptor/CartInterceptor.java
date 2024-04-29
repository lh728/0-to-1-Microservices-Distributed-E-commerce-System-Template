package com.ecommercesystemtemplate.cart.interceptor;

import com.ecommercesystemtemplate.cart.vo.UserInfoTo;
import com.ecommercesystemtemplate.common.constant.AuthServerConstant;
import com.ecommercesystemtemplate.common.constant.CartConstant;
import com.ecommercesystemtemplate.common.vo.MemberResponseVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *  before user login, intercept the request and determine whether the user is logged in
 *  then encapsulate to controller
 */
@Component
public class CartInterceptor implements HandlerInterceptor {

    public static ThreadLocal<UserInfoTo> threadLocal = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        UserInfoTo userInfoTo = new UserInfoTo();
        HttpSession session = request.getSession();
        MemberResponseVo attribute = (MemberResponseVo) session.getAttribute(AuthServerConstant.LOGIN_USER);
        if (attribute != null){
            userInfoTo.setUserId(attribute.getId());
        }

        Cookie[] cookies = request.getCookies();
        if (cookies != null){
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(CartConstant.TEMP_USER_COOKIE_NAME)){
                    userInfoTo.setUserKey(cookie.getValue());
                    userInfoTo.setTempUser(true);
                }
            }
        }

        // if user is not logged in, create a temporary user
        if (StringUtils.isEmpty(userInfoTo.getUserKey())){
            String uuid = java.util.UUID.randomUUID().toString();
            userInfoTo.setUserKey(uuid);
        }

        // before target method is executed, put userInfoTo into threadLocal
        threadLocal.set(userInfoTo);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (threadLocal.get().isTempUser()){
            // if user is not logged in, create a temporary user
            // put userKey into cookie
            Cookie cookie = new Cookie(CartConstant.TEMP_USER_COOKIE_NAME, threadLocal.get().getUserKey());
            cookie.setDomain("thellumall.com");
            cookie.setMaxAge(CartConstant.TEMP_USER_COOKIE_TIMEOUT);
            response.addCookie(cookie);
        }
    }
}
