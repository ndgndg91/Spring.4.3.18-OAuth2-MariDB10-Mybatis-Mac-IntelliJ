package com.ndgndg91.common;


import lombok.extern.log4j.Log4j;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Log4j
public class CheckLocaleInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info(request.getQueryString());
        log.info(request.getParameter("lang"));
        log.info(request.getLocale().toString());
        return super.preHandle(request, response, handler);
    }
}
