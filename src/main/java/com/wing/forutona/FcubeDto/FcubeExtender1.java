package com.wing.forutona.FcubeDto;

import lombok.Data;

import java.util.Date;

@Data
public class FcubeExtender1 extends Fcube{
    String nickname;
    String profilepictureurl;
    String distancewithme;
    String contenttype;
    String contentvalue;
    Date positionupdatetime;
    double userlevel;
    String fcmtoken;
    Integer followcount;
    double distance;
    double InfluencePower;

}
