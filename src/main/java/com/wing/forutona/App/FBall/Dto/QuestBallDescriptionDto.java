package com.wing.forutona.App.FBall.Dto;

import com.wing.forutona.App.FBall.Domain.QuestSelectMode;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
public class QuestBallDescriptionDto extends IssueBallDescriptionDto{


    QuestSelectMode successSelectMode;

    Double checkInPositionLat;
    Double checkInPositionLong;
    String checkInAddress;
    String photoCertificationDescription;
    Integer limitTimeSec;
    Double startPositionLat;
    Double startPositionLong;
    String startPositionAddress;
    Boolean timeLimitFlag;
    Boolean startPositionFlag;
    Boolean isOpenCheckInPosition;
    String qualifyingForQuestText;
    String checkInPositionDescription;
}
