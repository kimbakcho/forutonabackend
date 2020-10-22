package com.wing.forutona.CustomUtil;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class ResponseAddJsonHeaderInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(handler == null){
            return true;
        }
        if(handler instanceof HandlerMethod ){
            HandlerMethod method = (HandlerMethod) handler;
            if(method.getMethodAnnotation(ResponseAddJsonHeader.class) != null){
                response.setCharacterEncoding("utf-8");
                response.setContentType("application/json");
                return true;
            }
            return true;
        } else {
            return true;
        }

    }
}
