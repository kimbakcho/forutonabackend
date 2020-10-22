package com.wing.forutona.CustomUtil;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * FireBase Auth Token 을 넣어 주는 Inject
 */

public class FireBaseHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Autowired
    FireBaseAdmin fireBaseAdmin;

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterType().equals(FFireBaseToken.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        String header = nativeWebRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if(!StringUtil.isNullOrEmpty(header)){
            String token = header.replace("Bearer ", "");
            FirebaseAuth instance = FirebaseAuth.getInstance();
            FirebaseToken firebaseToken = instance.verifyIdToken(token, true);
            return new FFireBaseToken(firebaseToken,instance);
        }
        return null;
    }
}
