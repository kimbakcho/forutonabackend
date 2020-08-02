package com.wing.forutona.FBall.Dto;

import com.querydsl.core.annotations.QueryProjection;
import com.wing.forutona.FBall.Domain.FBallReply;
import com.wing.forutona.FBall.Domain.FBallValuation;
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
    Long ballLike = 0L;
    Long ballDislike = 0L;
    Long point = 0L;


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
        if(fBallValuation != null){
            this.ballLike = fBallValuation.getBallLike() == null ? 0 : fBallValuation.getBallLike();
            this.ballLike = fBallValuation.getBallDislike() == null ? 0 : fBallValuation.getBallDislike();
            this.point = fBallValuation.getPoint() == null ? 0 : fBallValuation.getPoint();
        }
    }

}
