package com.wing.forutona.CustomUtil;

import com.google.firebase.auth.FirebaseToken;
import lombok.Getter;
import lombok.Setter;

/**
 * FireBaseHandlerMethodArgumentResolver
 * 해당 Resolver로 인증 FirebaseToken을 객체를 넣어줌
 */
@Getter
@Setter
public class FFireBaseToken {
    FirebaseToken fireBaseToken;

    public FFireBaseToken(FirebaseToken fireBaseToken) {
        this.fireBaseToken = fireBaseToken;
    }
}
