package com.wing.forutona.FTag.Dto;

import com.wing.forutona.FTag.Domain.FBalltag;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class TagResDto {
    String ballUuid;
    String tagItem;

    public TagResDto(FBalltag fBalltag){
        this.ballUuid = fBalltag.getBallUuid().getBallUuid();
        this.tagItem = fBalltag.getTagItem();
    }
}
