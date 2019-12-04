package com.wing.forutona.FcubeDto;

import lombok.Data;

import java.util.Date;

@Data
public class FcubeExtender1 extends Fcube{
    String nickname;
    String profilepicktureurl;
    String distancewithme;
    String contenttype;
    String contentvalue;
    Date positionupdatetime;
    double userlevel;

}
