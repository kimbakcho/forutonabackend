package com.wing.forutona.FBall.Dto;

import com.querydsl.core.annotations.QueryProjection;
import com.wing.forutona.FBall.Domain.FBallReply;
import com.wing.forutona.ForutonaUser.Domain.FUserInfo;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class FBallReplyResDto {
    Long idx;
    String ballUuid;
    String uid;
    Long replyNumber;
    Long replySort;
    Long replyDepth;
    String replyText;
    LocalDateTime replyUploadDateTime;
    LocalDateTime replyUpdateDateTime;
    String userNickName;
    String userProfilePictureUrl;
    Boolean deleteFlag;

    @QueryProjection
    public FBallReplyResDto(FBallReply fBallReply){
        this.idx = fBallReply.getIdx();
        this.ballUuid = fBallReply.getReplyBallUuid().getBallUuid();
        this.uid = fBallReply.getReplyUid().getUid();
        this.replyNumber = fBallReply.getReplyNumber();
        this.replySort = fBallReply.getReplySort();
        this.replyDepth = fBallReply.getReplyDepth();
        this.replyText = fBallReply.getReplyText();
        this.replyUploadDateTime = fBallReply.getReplyUploadDateTime();
        this.userNickName = fBallReply.getReplyUid().getNickName();
        this.userProfilePictureUrl = fBallReply.getReplyUid().getProfilePictureUrl();
        this.replyUpdateDateTime = fBallReply.getReplyUpdateDateTime();
        this.deleteFlag = fBallReply.getDeleteFlag();

    }

}
