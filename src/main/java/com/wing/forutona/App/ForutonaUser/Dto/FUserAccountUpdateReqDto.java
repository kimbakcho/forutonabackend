package com.wing.forutona.App.ForutonaUser.Dto;

import com.wing.forutona.App.ForutonaUser.Domain.GenderType;
import lombok.Data;

@Data
public class FUserAccountUpdateReqDto {
    String isoCode;
    String nickName;
    String selfIntroduction;
    GenderType gender;
    boolean profileImageIsEmpty;
    boolean backGroundIsEmpty;
}
