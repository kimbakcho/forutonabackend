package com.wing.forutona.FBall.Domain;

import com.wing.forutona.ForutonaUser.Domain.FUserInfo;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class FBallReply {
    @Id
    String replyUuid;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ballUuid")
    FBall replyBallUuid;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid")
    FUserInfo replyUid;
    Long replyNumber;
    Long replySort;
    Long replyDepth;
    String replyText;
    LocalDateTime replyUploadDateTime;
    LocalDateTime replyUpdateDateTime;
    Boolean deleteFlag = false;

    @Builder
    public FBallReply(String replyUuid,FBall replyBallUuid
            ,FUserInfo replyUid
            ,Long replyNumber
            ,Long replySort
            ,String replyText
            ,LocalDateTime replyUploadDateTime
            ,LocalDateTime replyUpdateDateTime
            ,Long replyDepth ){
        this.replyUuid = replyUuid;
        this.replyBallUuid = replyBallUuid;
        this.replyUid = replyUid;
        this.replyNumber = replyNumber;
        this.replySort = replySort;
        this.replyText = replyText;
        this.replyUploadDateTime = replyUploadDateTime;
        this.replyUpdateDateTime = replyUpdateDateTime;
        this.replyDepth = replyDepth;

    }

    public void setReplyNumber(long replyNumber) {
        this.replyNumber = replyNumber;
    }

    public void setReplySort(long replySort) {
        this.replySort = replySort;
    }

    public void setReplyDepth(long replyDepth) {
        this.replyDepth = replyDepth;
    }

    public void setReplyUid(FUserInfo replyUid) {
        this.replyUid = replyUid;
    }

    public void setReplyText(String replyText) {
        this.replyText = replyText;
    }

    public void setReplyUploadDateTime(LocalDateTime replyUploadDateTime) {
        this.replyUploadDateTime = replyUploadDateTime;
    }

    public void setReplyUpdateDateTime(LocalDateTime replyUploadDateTime) {
        this.replyUpdateDateTime = replyUploadDateTime;
    }

    public void delete(){
        this.replyText = "Delete Text";
        this.replyUpdateDateTime = LocalDateTime.now();
        this.deleteFlag = true;
    }

    public FUserInfo getBallMakerUerInfo(){
        return replyBallUuid.getUid();
    }

    public long addFBallReplyCount(){
        return replyBallUuid.addBallReplyCount();
    }
}
