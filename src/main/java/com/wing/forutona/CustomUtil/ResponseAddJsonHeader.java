package com.wing.forutona.CustomUtil;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 비동기 타입의 ResponseEmitter을 사용할때 Response의 Header를 매번 지정해줘야함 그래서
 * 해당 어노테이션으로 헤더 타입을 추가 해줌.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ResponseAddJsonHeader {
}
