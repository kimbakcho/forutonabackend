package com.wing.forutona.FcubeDto;

import lombok.Data;

import java.util.Date;

/**
 * Player 자신이 참가한 Cube 조회용
 */
@Data
public class FcubeplayerExtender2 extends Fcubeplayer{
   String cubename;
   String cubetype;
   String makeruid;
   String makernickname;
   String makerprofilepicktureurl;
   double latitude;
   double longitude;

}