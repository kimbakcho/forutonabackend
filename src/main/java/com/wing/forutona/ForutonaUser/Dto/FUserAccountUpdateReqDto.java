package com.wing.forutona.ForutonaUser.Dto;

import lombok.Data;

@Data
public class FUserAccountUpdateReqDto {
    String isoCode;
    String nickName;
    String selfIntroduction;
    String userProfileImageUrl;

}
