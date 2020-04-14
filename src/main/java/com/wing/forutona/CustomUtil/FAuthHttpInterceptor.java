package com.wing.forutona.CustomUtil;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.wing.forutona.AuthDao.FireBaseAdmin;
import io.netty.util.internal.StringUtil;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class FAuthHttpInterceptor implements HandlerInterceptor {

    @Autowired
    private FireBaseAdmin fireBaseAdmin;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(handler == null){
            return true;
        }
        HandlerMethod method = (HandlerMethod) handler;
        if(method.getMethodAnnotation(AuthFireBaseJwtCheck.class) != null){
            return authFireBaseJwtCheckAnno(request, method);
        }else  {
            return true;
        }
    }


    /**
     * FireBase jwt 무결성을 체크하는 부분
     * @param request
     * @param method
     * @return
     * @throws FirebaseAuthException
     */
    private boolean authFireBaseJwtCheckAnno(HttpServletRequest request, HandlerMethod method) throws FirebaseAuthException {
        AuthFireBaseJwtCheck authFireBaseJwtCheck = method.getMethodAnnotation(AuthFireBaseJwtCheck.class);
        if (authFireBaseJwtCheck == null || authFireBaseJwtCheck.needCheck() == false) {
            return true;
        } else {
            String token = request.getHeader(HttpHeaders.AUTHORIZATION);
            token = token.replace("Bearer ", "");
            FirebaseToken firebaseToken = FirebaseAuth.getInstance().verifyIdToken(token, true);
            return true;
        }
    }
}
