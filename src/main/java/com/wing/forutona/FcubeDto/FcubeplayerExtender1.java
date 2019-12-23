package com.wing.forutona.FcubeDto;

import lombok.Data;

import java.util.Date;

/**
 * 참가자 조회 입력 확장 객체
 */
@Data
public class FcubeplayerExtender1 extends Fcubeplayer{
   String nickname;
   String profilepicktureurl;
   double latitude;
   double longitude;
   Date positionupdatetime;
   double userlevel;
   String fcmtoken;
}