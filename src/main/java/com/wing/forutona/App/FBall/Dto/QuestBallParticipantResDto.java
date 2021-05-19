package com.wing.forutona.App.FBall.Dto;

import com.wing.forutona.App.FBall.Domain.FBall;
import com.wing.forutona.App.FBall.Domain.QuestBallParticipant;
import com.wing.forutona.App.FBall.Value.QuestBallParticipateState;
import com.wing.forutona.App.ForutonaUser.Domain.FUserInfo;
import com.wing.forutona.App.ForutonaUser.Dto.FUserInfoSimpleResDto;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
public class QuestBallParticipantResDto {

    Integer idx;

    String ballUuid;

    FUserInfoSimpleResDto uid;

    LocalDateTime participationTime;

    String photoShotForCertificationImage;

    Double checkInPositionLat;

    Double checkInPositionLng;

    Double startPositionLat;

    Double startPositionLng;

    Integer likePoint;

    Integer dislikePoint;

    QuestBallParticipateState currentState;

    LocalDateTime acceptTime;

    public static QuestBallParticipantResDto fromQuestBallParticipant(QuestBallParticipant questBallParticipant){
        QuestBallParticipantResDto questBallParticipantResDto = new QuestBallParticipantResDto();
        questBallParticipantResDto.setBallUuid(questBallParticipant.getBallUuid().getBallUuid());
        questBallParticipantResDto.setIdx(questBallParticipant.getIdx());
        questBallParticipantResDto.setUid(new FUserInfoSimpleResDto(questBallParticipant.getUid()));
        questBallParticipantResDto.setCheckInPositionLat(questBallParticipant.getCheckInPositionLat());
        questBallParticipantResDto.setCheckInPositionLng(questBallParticipant.getCheckInPositionLng());
        questBallParticipantResDto.setCurrentState(questBallParticipant.getCurrentState());
        questBallParticipantResDto.setDislikePoint(questBallParticipant.getDislikePoint());
        questBallParticipantResDto.setLikePoint(questBallParticipant.getLikePoint());
        questBallParticipantResDto.setParticipationTime(questBallParticipant.getParticipationTime());
        questBallParticipantResDto.setPhotoShotForCertificationImage(questBallParticipant.getPhotoShotForCertificationImage());
        questBallParticipantResDto.setStartPositionLat(questBallParticipant.getStartPositionLat());
        questBallParticipantResDto.setStartPositionLng(questBallParticipant.getStartPositionLng());
        questBallParticipantResDto.setAcceptTime(questBallParticipant.getAcceptTime());
        return questBallParticipantResDto;
    }

}
