package com.wing.forutona;

import com.wing.forutona.CustomUtil.FAuthHttpInterceptor;
import com.wing.forutona.CustomUtil.FireBaseHandlerMethodArgumentResolver;
import com.wing.forutona.CustomUtil.ResponseAddJsonHeaderInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
//
//    @Autowired
//    private FAuthHttpInterceptor fAuthHttpInterceptor;
//
//    @Autowired
//    private ResponseAddJsonHeaderInterceptor responseAddJsonHeaderInterceptor;
//
//    @Autowired
//    private FireBaseHandlerMethodArgumentResolver fireBaseHandlerMethodArgumentResolver;
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(fAuthHttpInterceptor)
//                .addPathPatterns("/**");
//
//        registry.addInterceptor(responseAddJsonHeaderInterceptor)
//                .addPathPatterns("/**");
//
//    }
//
//    @Override
//    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
//        argumentResolvers.add(fireBaseHandlerMethodArgumentResolver);
//    }

}
