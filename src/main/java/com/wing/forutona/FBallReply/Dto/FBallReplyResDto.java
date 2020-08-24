package com.wing.forutona.FBallReply.Dto;

import com.querydsl.core.annotations.QueryProjection;
import com.wing.forutona.FBall.Dto.FBallResDto;
import com.wing.forutona.FBallValuation.Dto.FBallValuationResDto;
import com.wing.forutona.FBallReply.Domain.FBallReply;
import com.wing.forutona.FBallValuation.Domain.FBallValuation;
import com.wing.forutona.ForutonaUser.Dto.FUserInfoSimpleResDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class FBallReplyResDto {
    String replyUuid;
    FBallResDto ballUuid;
    FUserInfoSimpleResDto uid;
    Long replyNumber;
    Long replySort;
    Long replyDepth;
    String replyText;
    LocalDateTime replyUploadDateTime;
    LocalDateTime replyUpdateDateTime;
    String userNickName;
    String userProfilePictureUrl;
    Boolean deleteFlag;
    FBallValuationResDto fballValuationResDto;
    Long childCount;

    @QueryProjection
    public FBallReplyResDto(FBallReply fBallReply, FBallValuation fBallValuation) {
        this.replyUuid = fBallReply.getReplyUuid();
        this.ballUuid = new FBallResDto(fBallReply.getReplyBallUuid());
        this.uid = new FUserInfoSimpleResDto(fBallReply.getReplyUid());
        this.replyNumber = fBallReply.getReplyNumber();
        this.replySort = fBallReply.getReplySort();
        this.replyDepth = fBallReply.getReplyDepth();
        this.replyText = fBallReply.getReplyText();
        this.replyUploadDateTime = fBallReply.getReplyUploadDateTime();
        this.userNickName = fBallReply.getReplyUid().getNickName();
        this.userProfilePictureUrl = fBallReply.getReplyUid().getProfilePictureUrl();
        this.replyUpdateDateTime = fBallReply.getReplyUpdateDateTime();
        this.deleteFlag = fBallReply.getDeleteFlag();
        this.childCount = 0L;
        if(fBallValuation != null){
            this.fballValuationResDto = new FBallValuationResDto(fBallValuation);
        }
    }

}
