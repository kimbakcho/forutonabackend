package com.wing.forutona.ForutonaUser.Dto;

import lombok.Data;

@Data
public class FUserSnsCheckJoinResDto {
    String snsUid;
    String pictureUrl;
    String email;
    String userSnsName;
    //회원 가입 여부
    boolean join;
}
