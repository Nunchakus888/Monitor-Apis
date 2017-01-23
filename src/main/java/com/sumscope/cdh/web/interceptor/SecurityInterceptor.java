package com.sumscope.cdh.web.interceptor;

import com.sumscope.cdh.web.domain.UserInfo;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by wenshuai.li on 2016/11/2.
 */
public class SecurityInterceptor implements HandlerInterceptor {
    private static final String[] allowNames = {"admin"};

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        if(session.getAttribute("USER") != null && check(((UserInfo)session.getAttribute("USER")).getUserName())){
            return true;
        }else{
            throw new SecurityException();
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

    private boolean check(String name){
        for(String allowName : allowNames){
            if(allowName.equals(name)){
                return true;
            }
        }
        return false;
    }
}
