package com.wing.forutona.FTag.Dto;

import com.wing.forutona.FBall.Dto.FBallResDto;
import com.wing.forutona.FTag.Domain.FBalltag;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class FBallTagResDto {
    Long idx;
    String tagItem;
    FBallResDto ballUuid;
    double TagBI;

    public FBallTagResDto(FBalltag fBalltag){
        this.idx = fBalltag.getIdx();
        this.tagItem = fBalltag.getTagItem();
        this.ballUuid = new FBallResDto(fBalltag.getBallUuid());
        double TagBI;
    }
}
